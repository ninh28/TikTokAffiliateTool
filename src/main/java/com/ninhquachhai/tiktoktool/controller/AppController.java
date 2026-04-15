package com.ninhquachhai.tiktoktool.controller;

import com.ninhquachhai.tiktoktool.model.ProductRequest;
import com.ninhquachhai.tiktoktool.model.VideoHistory;
import com.ninhquachhai.tiktoktool.service.PromptGeneratorService;
import com.ninhquachhai.tiktoktool.service.TikTokDownloadService;
import com.ninhquachhai.tiktoktool.service.VideoGenerationService;
import com.ninhquachhai.tiktoktool.service.VideoRenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {

    // Tự động nhận diện thư mục upload dựa trên OS
    private static Path getTempUploadDir() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return Paths.get("temp_uploads");
        }
        return Paths.get("/app/temp_uploads");
    }

    private final VideoGenerationService videoGenerationService;
    private final PromptGeneratorService  promptGeneratorService;
    private final VideoRenderService      videoRenderService;
    private final TikTokDownloadService   tiktokDownloadService;

    public AppController(VideoGenerationService videoGenerationService,
                         PromptGeneratorService promptGeneratorService,
                         VideoRenderService videoRenderService,
                         TikTokDownloadService tiktokDownloadService) {
        this.videoGenerationService = videoGenerationService;
        this.promptGeneratorService = promptGeneratorService;
        this.videoRenderService     = videoRenderService;
        this.tiktokDownloadService  = tiktokDownloadService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(Authentication authentication, 
                        @RequestParam(value = "view", required = false) String view,
                        Model model) {
        boolean isViewingAsUser = "user".equals(view);
        boolean isAdmin = false;
        String userEmail = null;

        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauthUser) {
            userEmail = oauthUser.getAttribute("email");
            isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            
            if (isAdmin && !isViewingAsUser) {
                return "redirect:/admin";
            }
        }
        
        if (isAdmin && isViewingAsUser) {
            model.addAttribute("history", new ArrayList<VideoHistory>());
            model.addAttribute("hideHistory", true);
        } else if (userEmail != null) {
            model.addAttribute("history", videoGenerationService.getUserHistory(userEmail));
        } else {
            model.addAttribute("history", new ArrayList<VideoHistory>());
        }
        
        ProductRequest defaultRequest = new ProductRequest("", "", 1, List.of());
        model.addAttribute("productRequest", defaultRequest);
        return "index_simple";
    }

    @GetMapping("/admin")
    public String adminDashboard(Authentication authentication, Model model) {
        model.addAttribute("history", videoGenerationService.getAllSystemHistory());
        model.addAttribute("totalUsers", videoGenerationService.getTotalUsers());
        model.addAttribute("totalReups", videoGenerationService.getTotalReups());
        model.addAttribute("allUsers", videoGenerationService.getAllUsers());
        return "admin_dashboard";
    }

    @PostMapping("/generate")
    public String generate(Authentication authentication,
                           @RequestParam(value = "video",  required = false) MultipartFile video,
                           @RequestParam(value = "tiktokUrl", required = false) String tiktokUrl,
                           @RequestParam(value = "isMirror",      required = false, defaultValue = "false") boolean isMirror,
                           @RequestParam(value = "rotate1Degree",     required = false, defaultValue = "false") boolean rotate1Degree,
                           @RequestParam(value = "isCrop",            required = false, defaultValue = "false") boolean isCrop,
                           @RequestParam(value = "addNoiseLayer",     required = false, defaultValue = "false") boolean addNoiseLayer,
                           @RequestParam(value = "addOverlayLayer",   required = false, defaultValue = "false") boolean addOverlayLayer,
                           @RequestParam(value = "addVignette",       required = false, defaultValue = "false") boolean addVignette,
                           @RequestParam(value = "applyColorFilter",  required = false, defaultValue = "false") boolean applyColorFilter,
                           @RequestParam(value = "brightnessMode",    required = false, defaultValue = "normal") String brightnessMode,
                           @RequestParam(value = "enableTrimming",    required = false, defaultValue = "false") boolean enableTrimming,
                           @RequestParam(value = "speedMode",         required = false, defaultValue = "normal") String speedMode,
                           @RequestParam(value = "muteOriginalAudio", required = false, defaultValue = "false") boolean muteOriginalAudio,
                           @RequestParam(value = "mixBackgroundMusic", required = false, defaultValue = "false") boolean mixBackgroundMusic,
                           @RequestParam(value = "subtitle",          required = false, defaultValue = "")      String  subtitle,
                           @RequestParam(value = "videoQuality",      required = false, defaultValue = "original") String videoQuality,
                           @ModelAttribute ProductRequest productRequest,
                           Model model) {
        Path savedFile = null;
        String userEmail = null;
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauthUser) {
            userEmail = oauthUser.getAttribute("email");
        }

        try {
            Path tempUploadDir = getTempUploadDir();
            Files.createDirectories(tempUploadDir);
            
            if (tiktokUrl != null && !tiktokUrl.isBlank()) {
                savedFile = tiktokDownloadService.downloadNoWatermark(tiktokUrl);
            } 
            else if (video != null && !video.isEmpty()) {
                String originalName = video.getOriginalFilename() != null ? video.getOriginalFilename() : "upload.mp4";
                savedFile = tempUploadDir.resolve(System.currentTimeMillis() + "_" + originalName);
                video.transferTo(savedFile);
            } 
            else {
                throw new RuntimeException("Vui lòng tải lên video hoặc dán liên kết TikTok!");
            }
            
            VideoRenderService.ReupOptions reupOpts = new VideoRenderService.ReupOptions(
                isMirror, rotate1Degree, isCrop, addNoiseLayer, addOverlayLayer, addVignette,
                applyColorFilter, brightnessMode, enableTrimming, speedMode, 
                muteOriginalAudio, mixBackgroundMusic, subtitle, videoQuality
            );

            model.addAttribute("response", videoGenerationService.generateScripts(productRequest, savedFile, reupOpts));
            
            if (userEmail != null) {
                model.addAttribute("history", videoGenerationService.getUserHistory(userEmail));
            }
            
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            if (userEmail != null) {
                model.addAttribute("history", videoGenerationService.getUserHistory(userEmail));
            }
        } finally {
            if (savedFile != null) {
                try { Files.deleteIfExists(savedFile); } catch (Exception ignored) {}
            }
        }
        
        return "index_simple";
    }

    @PostMapping("/delete-history/{id}")
    public String deleteHistory(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer) {
        videoGenerationService.deleteHistory(id);
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Authentication authentication, Exception e, Model model) {
        String userEmail = null;
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauthUser) {
            userEmail = oauthUser.getAttribute("email");
        }

        model.addAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        if (userEmail != null) {
            model.addAttribute("history", videoGenerationService.getUserHistory(userEmail));
        }
        model.addAttribute("productRequest", new ProductRequest("", "", 1, List.of()));
        return "index_simple";
    }
}
