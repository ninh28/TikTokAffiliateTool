package com.ninhquachhai.tiktoktool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninhquachhai.tiktoktool.model.*;
import com.ninhquachhai.tiktoktool.repository.AppUserRepository;
import com.ninhquachhai.tiktoktool.repository.VideoHistoryRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class VideoGenerationService {

    private final ExecutorService videoRenderExecutor = Executors.newFixedThreadPool(3);
    private final ConcurrentMap<String, String> statusMap = new ConcurrentHashMap<>();

    @Value("${groq.api.key:}")
    private String groqApiKey;

    private final VideoRenderService videoRenderService;
    private final LumaVideoService   lumaVideoService;
    private final VideoHistoryRepository videoHistoryRepository;
    private final AppUserRepository appUserRepository;

    public VideoGenerationService(VideoRenderService videoRenderService, 
                                  LumaVideoService lumaVideoService,
                                  VideoHistoryRepository videoHistoryRepository,
                                  AppUserRepository appUserRepository) {
        this.videoRenderService = videoRenderService;
        this.lumaVideoService   = lumaVideoService;
        this.videoHistoryRepository = videoHistoryRepository;
        this.appUserRepository = appUserRepository;
    }

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper  = new ObjectMapper();

    @PreDestroy
    public void cleanup() {
        videoRenderExecutor.shutdown();
        try {
            if (!videoRenderExecutor.awaitTermination(60, TimeUnit.SECONDS))
                videoRenderExecutor.shutdownNow();
        } catch (InterruptedException e) {
            videoRenderExecutor.shutdownNow();
        }
    }

    @Transactional
    public ProductResponse generateScripts(ProductRequest request, Path imagePath,
                                            VideoRenderService.ReupOptions reupOpts) throws Exception {
        System.out.println("🚀 OPTIMIZED REUP WORKFLOW: Bắt đầu tạo 1 video Reup tối ưu...");

        List<String> prompts = request.getVideoPrompts();
        if (prompts == null || prompts.isEmpty()) {
            prompts = getAutoAffiliatePrompts();
        }

        VideoScenario scenario = new VideoScenario(
            "Optimized Reup", 
            prompts.get(0), 
            "Sieu pham Reup da nang", 
            "Mua ngay tai TikTok Shop"
        );

        AtomicInteger completedCount = new AtomicInteger(0);
        List<VideoScript> scripts = new ArrayList<>();
        
        VideoScript script = produceVideo(imagePath, scenario, 1, completedCount, reupOpts);
        scripts.add(script);

        if (script.videoPath() != null) {
            saveToHistoryAndIncrementCounter(request.getProductName(), script.videoPath(), reupOpts);
        }

        return new ProductResponse(request.getProductName(), scripts);
    }

    private void saveToHistoryAndIncrementCounter(String productName, String videoPath, VideoRenderService.ReupOptions opts) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof OAuth2User oauthUser) {
                String email = oauthUser.getAttribute("email");
                
                VideoHistory history = VideoHistory.builder()
                        .userEmail(email)
                        .productName(productName != null && !productName.isBlank() ? productName : "Sản phẩm Reup")
                        .videoPath(videoPath)
                        .brightnessMode(opts.brightnessMode())
                        .subtitle(opts.subtitle())
                        .createdAt(LocalDateTime.now())
                        .build();
                videoHistoryRepository.save(history);

                appUserRepository.findById(email).ifPresent(user -> {
                    user.setReupCount(user.getReupCount() + 1);
                    appUserRepository.save(user);
                    System.out.println("📈 Tăng số lần reup cho user [" + email + "] lên: " + user.getReupCount());
                });
            }
        } catch (Exception e) {
            System.err.println("❌ Không thể cập nhật lịch sử/số lượng reup: " + e.getMessage());
        }
    }

    private VideoScript produceVideo(Path imagePath, VideoScenario scenario,
                                      int videoIndex, AtomicInteger completedCount,
                                      VideoRenderService.ReupOptions reupOpts) {
        updateStatus(scenario.name, "Đang khởi động...");
        String finalVideoPath = null;

        try {
            // Cập nhật lên 14 tham số để bao gồm videoQuality
            VideoRenderService.ReupOptions weaponOpts = new VideoRenderService.ReupOptions(
                reupOpts.isMirror(),
                reupOpts.rotate1Degree(),
                reupOpts.isCrop(),
                reupOpts.addNoiseLayer(),
                reupOpts.addOverlayLayer(),
                reupOpts.addVignette(),
                reupOpts.applyColorFilter(),
                reupOpts.brightnessMode(),
                reupOpts.enableTrimming(),
                reupOpts.speedMode(),
                reupOpts.muteOriginalAudio(),
                reupOpts.mixBackgroundMusic(),
                reupOpts.subtitle(),
                reupOpts.videoQuality() // Tham số mới
            );

            updateStatus(scenario.name, "Đang xử lý tối ưu video Reup (Công thức 9/10)...");
            finalVideoPath = videoRenderService.renderUltimateAffiliate(imagePath, weaponOpts);
            
            updateStatus(scenario.name, "Hoàn tất!");
            completedCount.incrementAndGet();

        } catch (Exception e) {
            updateStatus(scenario.name, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }

        return new VideoScript(
            scenario.name,
            scenario.hook,
            scenario.prompt,
            scenario.cta,
            finalVideoPath,
            true
        );
    }

    private void updateStatus(String key, String status) {
        statusMap.put(key, status);
        System.out.println("    [" + key + "] " + status);
    }

    private List<String> getAutoAffiliatePrompts() {
        return List.of("Automated affiliate visual enhancement");
    }

    public List<VideoHistory> getAllSystemHistory() {
        return videoHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public List<VideoHistory> getUserHistory(String userEmail) {
        return videoHistoryRepository.findByUserEmailOrderByCreatedAtDesc(userEmail);
    }

    public long getTotalUsers() { return appUserRepository.count(); }
    public long getTotalReups() {
        return appUserRepository.findAll().stream().mapToLong(AppUser::getReupCount).sum();
    }
    public List<AppUser> getAllUsers() { return appUserRepository.findAllByOrderByLastLoginDesc(); }

    @Transactional
    public void registerOrUpdateLogin(OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        if (email == null) return;

        Optional<AppUser> existingUser = appUserRepository.findById(email);
        if (existingUser.isPresent()) {
            AppUser user = existingUser.get();
            user.setLastLogin(LocalDateTime.now());
            user.setName(oauthUser.getAttribute("name"));
            user.setPicture(oauthUser.getAttribute("picture"));
            appUserRepository.save(user);
        } else {
            AppUser newUser = AppUser.builder()
                    .email(email)
                    .name(oauthUser.getAttribute("name"))
                    .picture(oauthUser.getAttribute("picture"))
                    .lastLogin(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .reupCount(0)
                    .build();
            appUserRepository.save(newUser);
            System.out.println("🆕 Đăng ký user mới: " + email);
        }
    }

    @Transactional
    public void deleteHistory(Long id) {
        videoHistoryRepository.findById(id).ifPresent(history -> {
            // Thử xóa file vật lý nếu cần thiết
            try {
                String pathStr = history.getVideoPath();
                if (pathStr != null && pathStr.startsWith("/")) {
                    // Giả sử videoPath lưu dạng web /outputs/abc.mp4, cần map về thư mục thực tế
                    Path fileToDelete = Paths.get("src/main/resources/static" + pathStr);
                    Files.deleteIfExists(fileToDelete);
                }
            } catch (IOException e) {
                System.err.println("❌ Không thể xóa file video: " + e.getMessage());
            }
            videoHistoryRepository.deleteById(id);
        });
    }
}
