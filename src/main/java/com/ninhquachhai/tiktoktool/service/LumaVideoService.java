package com.ninhquachhai.tiktoktool.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninhquachhai.tiktoktool.model.AiVideoJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Service
public class LumaVideoService {

    private static final String GENERATIONS_URL  = "https://api.lumalabs.ai/dream-machine/v1/generations";
    private static final String FILE_UPLOAD_URL  = "https://api.lumalabs.ai/dream-machine/v1/file_uploads";
    private static final int    POLL_INTERVAL_MS  = 10_000;
    private static final int    MAX_POLL_ATTEMPTS  = 30;   // 5 phút
    private static final int    MAX_RETRY_NETWORK  = 3;    // retry khi lỗi mạng nhẹ

    @Value("${luma.api.key:}")
    private String lumaApiKey;

    @Value("${app.base-url:http://localhost:8080}")
    private String appBaseUrl;

    // RestTemplate với timeout 30s cho từng request HTTP
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LumaVideoService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15_000);
        factory.setReadTimeout(30_000);
        this.restTemplate = new RestTemplate(factory);
    }

    public boolean isApiKeyConfigured() {
        return lumaApiKey != null
            && !lumaApiKey.isBlank()
            && !lumaApiKey.equals("your_luma_api_key_here");
    }

    /** Kiểm tra API key hợp lệ bằng cách gọi thử endpoint. */
    public void validateApiKey() throws Exception {
        try {
            restTemplate.exchange(GENERATIONS_URL + "?limit=1",
                HttpMethod.GET, new HttpEntity<>(buildHeaders()), String.class);
            System.out.println("✅ Luma API key hợp lệ.");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401)
                throw new RuntimeException("Sai API Key Luma — vui lòng kiểm tra luma.api.key trong application.properties");
            throw new RuntimeException("Luma API lỗi khi kiểm tra key: " + e.getStatusCode());
        }
    }

    /**
     * Upload ảnh → submit generation job → trả về AiVideoJob chứa jobId.
     */
    public AiVideoJob submitVideoJob(Path imagePath, String userPrompt, int videoIndex) throws Exception {
        System.out.println("🚀 Đang gửi yêu cầu lên Luma AI cho Video " + videoIndex + "...");

        String imageUrl = uploadImage(imagePath, videoIndex);

        Map<String, Object> frame0    = Map.of("type", "image", "url", imageUrl);
        Map<String, Object> keyframes = Map.of("frame0", frame0);
        Map<String, Object> body      = new HashMap<>();
        body.put("prompt",       buildEnrichedPrompt(userPrompt, videoIndex));
        body.put("keyframes",    keyframes);
        body.put("aspect_ratio", "9:16");
        body.put("loop",         false);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), buildHeaders());

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(GENERATIONS_URL, entity, String.class);
            JsonNode json  = objectMapper.readTree(response.getBody());
            String   jobId = json.path("id").asText();
            if (jobId == null || jobId.isBlank())
                throw new RuntimeException("Luma không trả về job ID. Body: " + response.getBody());

            System.out.println("✅ [Video " + videoIndex + "] Job submitted: " + jobId);
            return new AiVideoJob(jobId, videoIndex);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401)
                throw new RuntimeException("Sai API Key Luma — vui lòng kiểm tra luma.api.key");
            throw new RuntimeException("Luma API lỗi " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
        }
    }

    /**
     * Kiểm tra trạng thái job cụ thể (dùng cho Smart Polling)
     */
    public AiVideoJob checkJobStatus(String jobId, int videoIndex) throws Exception {
        String statusUrl = GENERATIONS_URL + "/" + jobId;
        HttpEntity<Void> entity = new HttpEntity<>(buildHeaders());
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                statusUrl, HttpMethod.GET, entity, String.class);
            
            String rawBody = response.getBody();
            JsonNode json = objectMapper.readTree(rawBody);
            String state = json.path("state").asText();
            
            AiVideoJob job = new AiVideoJob(jobId, videoIndex);
            
            switch (state) {
                case "completed" -> {
                    String videoUrl = json.path("assets").path("video").asText();
                    job.setStatus(AiVideoJob.Status.COMPLETED);
                    job.setVideoUrl(videoUrl);
                }
                case "failed" -> {
                    String reason = json.path("failure_reason").asText("Unknown");
                    job.setStatus(AiVideoJob.Status.FAILED);
                    job.setErrorMessage("Luma thất bại: " + reason);
                }
                default -> job.setStatus(AiVideoJob.Status.PROCESSING);
            }
            
            return job;
            
        } catch (HttpClientErrorException e) {
            AiVideoJob job = new AiVideoJob(jobId, videoIndex);
            job.setStatus(AiVideoJob.Status.FAILED);
            
            if (e.getStatusCode().value() == 401) {
                job.setErrorMessage("Lỗi: Sai API Key Luma (401)");
            } else if (e.getStatusCode().value() == 402) {
                job.setErrorMessage("Lỗi: Hết lượt tạo Video AI hoặc hết Credit (402)");
            } else {
                job.setErrorMessage("Luma lỗi HTTP " + e.getStatusCode().value() + ": " + e.getResponseBodyAsString());
            }
            
            return job;
        }
    }
    public AiVideoJob pollUntilComplete(AiVideoJob job) {
        String           statusUrl  = GENERATIONS_URL + "/" + job.getJobId();
        HttpEntity<Void> entity     = new HttpEntity<>(buildHeaders());
        int              netRetries = 0;

        for (int attempt = 0; attempt < MAX_POLL_ATTEMPTS; attempt++) {
            try {
                Thread.sleep(POLL_INTERVAL_MS);

                ResponseEntity<String> response = restTemplate.exchange(
                    statusUrl, HttpMethod.GET, entity, String.class);
                netRetries = 0;

                String   rawBody = response.getBody();
                JsonNode json    = objectMapper.readTree(rawBody);
                String   state   = json.path("state").asText();
                String   label   = mapStateToLabel(state);

                // In toàn bộ JSON để debug
                System.out.printf("⏳ [Video %d] %s (%d/%d) | JSON: %s%n",
                    job.getVideoIndex(), label, attempt + 1, MAX_POLL_ATTEMPTS, rawBody);

                switch (state) {
                    case "completed" -> {
                        String videoUrl = json.path("assets").path("video").asText();
                        job.setStatus(AiVideoJob.Status.COMPLETED);
                        job.setVideoUrl(videoUrl);
                        System.out.println("🎉 [Video " + job.getVideoIndex() + "] Hoàn thành: " + videoUrl);
                        return job;
                    }
                    case "failed" -> {
                        String reason = json.path("failure_reason").asText("Unknown");
                        job.setStatus(AiVideoJob.Status.FAILED);
                        job.setErrorMessage("Luma thất bại: " + reason);
                        System.err.println("❌ [Video " + job.getVideoIndex() + "] " + reason);
                        return job;
                    }
                    default -> job.setStatus(AiVideoJob.Status.PROCESSING);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                job.setStatus(AiVideoJob.Status.FAILED);
                job.setErrorMessage("Polling bị ngắt");
                return job;

            } catch (HttpClientErrorException e) {
                // Bất kỳ mã lỗi HTTP (400, 401, 403, 500...) → dừng ngay, không tiếp tục polling
                String errBody = e.getResponseBodyAsString();
                System.err.printf("❌ [Video %d] HTTP %d dừng polling. Body: %s%n",
                    job.getVideoIndex(), e.getStatusCode().value(), errBody);
                job.setStatus(AiVideoJob.Status.FAILED);
                if (e.getStatusCode().value() == 401)
                    job.setErrorMessage("Sai API Key Luma (401)");
                else
                    job.setErrorMessage("Luma lỗi HTTP " + e.getStatusCode().value() + ": " + errBody);
                return job;

            } catch (ResourceAccessException e) {
                // Lỗi mạng nhẹ → retry tối đa 3 lần
                netRetries++;
                System.err.printf("⚠️ [Video %d] Lỗi mạng (retry %d/%d): %s%n",
                    job.getVideoIndex(), netRetries, MAX_RETRY_NETWORK, e.getMessage());
                if (netRetries >= MAX_RETRY_NETWORK) {
                    job.setStatus(AiVideoJob.Status.FAILED);
                    job.setErrorMessage("Lỗi mạng sau " + MAX_RETRY_NETWORK + " lần thử");
                    return job;
                }

            } catch (Exception e) {
                System.err.println("⚠️ [Video " + job.getVideoIndex() + "] Polling lỗi: " + e.getMessage());
            }
        }

        // Timeout
        job.setStatus(AiVideoJob.Status.FAILED);
        job.setErrorMessage("Timeout: video chưa xong sau " + (MAX_POLL_ATTEMPTS * POLL_INTERVAL_MS / 1000) + "s — vui lòng thử lại hoặc dùng FFmpeg");
        System.err.println("⏰ [Video " + job.getVideoIndex() + "] Timeout 5 phút");
        return job;
    }

    /** Map state API → thông báo tiếng Việt thân thiện */
    private String mapStateToLabel(String state) {
        return switch (state) {
            case "queued"     -> "Đang chờ hàng đợi";
            case "dreaming"   -> "AI đang vẽ video...";
            case "processing" -> "Đang tải video về server";
            case "completed"  -> "Hoàn thành";
            case "failed"     -> "Thất bại";
            default           -> "Trạng thái: " + state;
        };
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Upload ảnh lên Luma: POST tạo session → PUT file lên presigned S3 URL.
     * Nếu Luma không trả về public_url hợp lệ, tự động copy ảnh vào static/temp
     * và dùng localhost URL để Luma có thể truy cập (chỉ hoạt động khi server có IP public).
     */
    private String uploadImage(Path imagePath, int videoIndex) throws Exception {
        String filename = imagePath.getFileName().toString();
        String mimeType = detectMimeType(filename);

        Map<String, Object> uploadReq = Map.of("file_name", filename, "mime_type", mimeType);
        HttpEntity<String> sessionEntity = new HttpEntity<>(
            objectMapper.writeValueAsString(uploadReq), buildHeaders());

        try {
            ResponseEntity<String> sessionResp = restTemplate.postForEntity(
                FILE_UPLOAD_URL, sessionEntity, String.class);
            System.out.println("[Luma Upload Session] " + sessionResp.getBody());

            JsonNode sessionJson = objectMapper.readTree(sessionResp.getBody());
            String uploadUrl = sessionJson.path("upload_url").asText();
            String publicUrl = sessionJson.path("public_url").asText();

            if (uploadUrl != null && !uploadUrl.isBlank()) {
                // PUT file lên S3 presigned URL
                HttpHeaders putHeaders = new HttpHeaders();
                putHeaders.setContentType(MediaType.parseMediaType(mimeType));
                restTemplate.put(uploadUrl, new HttpEntity<>(Files.readAllBytes(imagePath), putHeaders));

                if (publicUrl != null && publicUrl.startsWith("https://")) {
                    System.out.println("📎 [Video " + videoIndex + "] Ảnh đã upload Luma S3: " + publicUrl);
                    return publicUrl;
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ [Video " + videoIndex + "] Luma upload thất bại: " + e.getMessage());
        }

        // Fallback: copy ảnh vào static/temp/ và dùng localhost URL
        Path staticTemp = Paths.get("src/main/resources/static/temp");
        Files.createDirectories(staticTemp);
        Path dest = staticTemp.resolve(filename);
        Files.copy(imagePath, dest, StandardCopyOption.REPLACE_EXISTING);
        String localUrl = appBaseUrl + "/temp/" + filename;
        System.out.println("📎 [Video " + videoIndex + "] Dùng localhost URL: " + localUrl);
        System.out.println("⚠️ Lưu ý: Luma chỉ truy cập được nếu server có IP public hoặc dùng ngrok.");
        return localUrl;
    }

    private String buildEnrichedPrompt(String userPrompt, int videoIndex) {
        String style = switch (videoIndex) {
            case 1 -> "Dynamic 360-degree orbiting camera movement, extreme macro focus shift revealing texture details, " +
                     "cinematic product review style, slow smooth pan left to right, natural daylight studio setup, " +
                     "professional depth of field transition, 4K ultra-sharp quality";
                     
            case 2 -> "Smooth gimbal tracking shot with cinematic depth of field changing dynamically, " +
                     "soft lighting shifting from warm to cool tones, POV lifestyle perspective, " +
                     "gentle zoom-in revealing product beauty, warm atmospheric lighting, vertical 9:16 composition";
                     
            case 3 -> "Hyper-lapse zoom with dynamic perspective change, energetic camera movement with quick cuts feel, " +
                     "dynamic commercial shot style, dramatic zoom-out reveal transition, " +
                     "vibrant saturated colors with high contrast, fast-paced energetic motion";
                     
            default -> "cinematic product shot with professional studio lighting, smooth camera movement";
        };
        
        return userPrompt + ". Visual style: " + style + 
               ". Camera work: professional product videography with dynamic movement. " +
               "No text overlay, no watermark, no people, focus on product only.";
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.set("Authorization", "Bearer " + lumaApiKey);
        return h;
    }

    private String detectMimeType(String filename) {
        if (filename == null) return "image/jpeg";
        String f = filename.toLowerCase();
        if (f.endsWith(".png"))  return "image/png";
        if (f.endsWith(".webp")) return "image/webp";
        if (f.endsWith(".gif"))  return "image/gif";
        return "image/jpeg";
    }
}
