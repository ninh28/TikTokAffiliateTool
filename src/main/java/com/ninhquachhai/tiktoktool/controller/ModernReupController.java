package com.ninhquachhai.tiktoktool.controller;

import com.ninhquachhai.tiktoktool.model.VideoReupJob;
import com.ninhquachhai.tiktoktool.service.ReupJobService;
import com.ninhquachhai.tiktoktool.service.SingleVideoReupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Modern Re-up Controller
 * Xử lý 1 video re-up duy nhất
 */
@Controller
@RequestMapping("/reup")
public class ModernReupController {

    private static final Logger LOG = Logger.getLogger(ModernReupController.class.getName());
    private static final Path TEMP_UPLOAD_DIR = Paths.get("temp_uploads/modern");

    private final ReupJobService reupJobService;
    private final SingleVideoReupService singleVideoReupService;

    public ModernReupController(ReupJobService reupJobService,
                               SingleVideoReupService singleVideoReupService) {
        this.reupJobService = reupJobService;
        this.singleVideoReupService = singleVideoReupService;
    }

    @GetMapping("/modern")
    public String modernReupPage() {
        return "reup_modern";
    }

    @PostMapping("/api/upload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadVideo(
            @RequestParam("video") MultipartFile video,
            @RequestParam(value = "subtitle", required = false, defaultValue = "") String subtitle,
            @RequestParam(value = "isAutoMode", required = false, defaultValue = "true") boolean isAutoMode,
            @RequestParam(value = "applyCrop", required = false, defaultValue = "true") boolean applyCrop,
            @RequestParam(value = "applySpeed", required = false, defaultValue = "true") boolean applySpeed,
            @RequestParam(value = "applyMirror", required = false, defaultValue = "false") boolean applyMirror,
            @RequestParam(value = "removeAudio", required = false, defaultValue = "false") boolean removeAudio) {

        try {
            Map<String, Object> response = new HashMap<>();

            if (video == null || video.isEmpty()) {
                response.put("success", false);
                response.put("error", "Vui lòng chọn video để upload");
                return ResponseEntity.badRequest().body(response);
            }

            String originalName = video.getOriginalFilename();
            if (!isValidVideoFormat(originalName)) {
                response.put("success", false);
                response.put("error", "Định dạng video không hỗ trợ.");
                return ResponseEntity.badRequest().body(response);
            }

            Files.createDirectories(TEMP_UPLOAD_DIR);
            String fileName = System.currentTimeMillis() + "_" + originalName;
            Path uploadedFilePath = TEMP_UPLOAD_DIR.resolve(fileName);
            video.transferTo(uploadedFilePath);

            VideoReupJob job = reupJobService.createJob(originalName, uploadedFilePath.toString());
            job.setSubtitleText(subtitle);
            job.setApplyCrop(isAutoMode || applyCrop);
            job.setSpeedMultiplier(isAutoMode || applySpeed ? 1.1 : 1.0);
            job.setApplyMirror(!isAutoMode && applyMirror);
            
            // LƯU Ý: Trường removeAudio được gán vào addWatermark trong model VideoReupJob hiện tại (dựa trên code cũ)
            // Hoặc nếu model đã có field muteAudio/removeAudio thì dùng field đó.
            job.setAddWatermark(removeAudio); 

            reupJobService.updateJobStatus(job.getJobId(), VideoReupJob.JobStatus.QUEUED);

            response.put("success", true);
            response.put("jobId", job.getJobId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/api/process/{jobId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processVideo(@PathVariable String jobId) {
        try {
            VideoReupJob job = reupJobService.getJob(jobId);
            Map<String, Object> response = new HashMap<>();

            if (job == null) {
                response.put("success", false);
                response.put("error", "Job không tìm thấy");
                return ResponseEntity.badRequest().body(response);
            }

            new Thread(() -> {
                try {
                    Path inputPath = Paths.get(job.getFilePath());
                    
                    // CHỨC NĂNG TẮT ÂM THANH: 
                    // removeAudio được lấy từ job.isAddWatermark() (Mapping từ upload)
                    boolean removeAudio = job.isAddWatermark();

                    VideoReupJob updatedJob = singleVideoReupService.processVideoReup(
                        job, inputPath, job.isApplyCrop(), (job.getSpeedMultiplier() > 1.0), 
                        job.isApplyMirror(), removeAudio, job.getSubtitleText()
                    );

                    reupJobService.completeJob(jobId, updatedJob.getOutputVideoPaths());
                } catch (Exception e) {
                    reupJobService.setJobError(jobId, e.getMessage());
                }
            }).start();

            response.put("success", true);
            response.put("jobId", jobId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping({"/api/status/{jobId}", "/api/job-status/{jobId}"})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getJobStatus(@PathVariable String jobId) {
        try {
            VideoReupJob job = reupJobService.getJob(jobId);
            Map<String, Object> response = new HashMap<>();
            if (job == null) return ResponseEntity.notFound().build();

            response.put("success", true);
            response.put("status", job.getStatus().toString());
            response.put("progress", job.getProgressPercent());

            if (job.getStatus() == VideoReupJob.JobStatus.COMPLETED) {
                if (job.getOutputVideoPaths() != null && !job.getOutputVideoPaths().isEmpty()) {
                    response.put("outputPath", job.getOutputVideoPaths().get(0));
                }
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/download/{jobId}/{variantIndex}")
    public ResponseEntity<byte[]> downloadVariant(@PathVariable String jobId, @PathVariable int variantIndex) {
        try {
            VideoReupJob job = reupJobService.getJob(jobId);
            if (job == null || job.getOutputVideoPaths() == null) return ResponseEntity.notFound().build();

            Path videoPath = Paths.get("src/main/resources/static" + job.getOutputVideoPaths().get(0));
            if (!Files.exists(videoPath)) videoPath = Paths.get(job.getOutputVideoPaths().get(0));

            byte[] videoData = Files.readAllBytes(videoPath);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"reup_video.mp4\"")
                    .header("Content-Type", "video/mp4")
                    .body(videoData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isValidVideoFormat(String fileName) {
        if (fileName == null) return false;
        String lower = fileName.toLowerCase();
        return lower.endsWith(".mp4") || lower.endsWith(".mov") || lower.endsWith(".avi") ||
               lower.endsWith(".mkv") || lower.endsWith(".webm") || lower.endsWith(".flv");
    }
}
