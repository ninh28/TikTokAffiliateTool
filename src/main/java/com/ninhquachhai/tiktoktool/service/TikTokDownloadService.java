package com.ninhquachhai.tiktoktool.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class TikTokDownloadService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TIKWM_API_URL = "https://www.tikwm.com/api/?url=";

    public Path downloadNoWatermark(String tiktokUrl) throws Exception {
        System.out.println("📥 Bắt đầu phân tích liên kết TikTok: " + tiktokUrl);
        
        // Gọi API trung gian để lấy link tải video không logo
        String response = restTemplate.getForObject(TIKWM_API_URL + tiktokUrl, String.class);
        JsonNode root = objectMapper.readTree(response);
        
        if (root.path("code").asInt() != 0) {
            throw new RuntimeException("Không thể lấy thông tin video. Vui lòng kiểm tra lại liên kết TikTok.");
        }

        // Lấy URL video không logo từ kết quả API
        String videoUrl = root.path("data").path("play").asText();
        if (videoUrl == null || videoUrl.isEmpty()) {
            throw new RuntimeException("Không tìm thấy link video không logo.");
        }

        // Tải video về thư mục tạm
        Path tempDir = Paths.get("temp_uploads");
        Files.createDirectories(tempDir);
        Path targetPath = tempDir.resolve("tiktok_" + UUID.randomUUID() + ".mp4");

        try (InputStream in = new URL(videoUrl).openStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        System.out.println("✅ Đã tải video TikTok không logo về: " + targetPath);
        return targetPath;
    }
}
