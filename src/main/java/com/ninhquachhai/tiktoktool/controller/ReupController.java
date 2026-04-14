package com.ninhquachhai.tiktoktool.controller;

import com.ninhquachhai.tiktoktool.model.VideoReupJob;
import com.ninhquachhai.tiktoktool.service.ReupJobService;
import com.ninhquachhai.tiktoktool.service.SingleVideoReupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Controller for video re-up functionality - Optimized for Single Flow
 */
@Controller
@RequestMapping("/reup")
public class ReupController {

    private static final Logger LOG = Logger.getLogger(ReupController.class.getName());
    private static final Path TEMP_UPLOAD_DIR = Paths.get("temp_uploads/reup");

    private final ReupJobService reupJobService;
    private final SingleVideoReupService singleVideoReupService;

    public ReupController(ReupJobService reupJobService, SingleVideoReupService singleVideoReupService) {
        this.reupJobService = reupJobService;
        this.singleVideoReupService = singleVideoReupService;
    }

    /**
     * Display re-up page
     */
    @GetMapping
    public String reupPage(Model model) {
        return "reup_new"; // Sử dụng template mới hỗ trợ single video
    }

    /**
     * Upload video and create re-up job
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadVideo(
            @RequestParam("video") MultipartFile video,
            @RequestParam(value = "applyMirror", required = false, defaultValue = "true") boolean applyMirror,
            @RequestParam(value = "applyCrop", required = false, defaultValue = "true") boolean applyCrop,
            @RequestParam(value = "speedMultiplier", required = false, defaultValue = "1.1") double speedMultiplier,
            @RequestParam(value = "subtitleText", required = false, defaultValue = "") String subtitleText) {

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
                response.put("error", "Định dạng không hỗ trợ");
                return ResponseEntity.badRequest().body(response);
            }

            Files.createDirectories(TEMP_UPLOAD_DIR);
            String fileName = System.currentTimeMillis() + "_" + originalName;
            Path uploadedFilePath = TEMP_UPLOAD_DIR.resolve(fileName);
            video.transferTo(uploadedFilePath);

            VideoReupJob job = reupJobService.createJob(originalName, uploadedFilePath.toString());
            job.setApplyMirror(applyMirror);
            job.setApplyCrop(applyCrop);
            job.setSpeedMultiplier(speedMultiplier);
            job.setSubtitleText(subtitleText);

            reupJobService.updateJobStatus(job.getJobId(), VideoReupJob.JobStatus.QUEUED);

            response.put("success", true);
            response.put("jobId", job.getJobId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LOG.severe("Upload error: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Start processing re-up job - Modified for SINGLE VIDEO return
     */
    @PostMapping("/process/{jobId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processJob(@PathVariable String jobId) {
        try {
            VideoReupJob job = reupJobService.getJob(jobId);
            Map<String, Object> response = new HashMap<>();

            if (job == null) {
                response.put("success", false);
                response.put("error", "Job không tìm thấy");
                return ResponseEntity.badRequest().body(response);
            }

            // Xử lý trong luồng riêng để tránh block request
            new Thread(() -> {
                try {
                    Path inputPath = Paths.get(job.getFilePath());
                    
                    // GỌI SERVICE XỬ LÝ 1 VIDEO DUY NHẤT
                    VideoReupJob result = singleVideoReupService.processVideoReup(
                        job, inputPath, job.isApplyCrop(), true, job.isApplyMirror(), false, job.getSubtitleText()
                    );

                    // Cập nhật trạng thái Job hoàn tất
                    reupJobService.completeJob(jobId, result.getOutputVideoPaths());
                    
                    LOG.info("✅ [ReupController] Single video processing completed for: " + jobId);
                } catch (Exception e) {
                    LOG.severe("Error processing job: " + e.getMessage());
                    reupJobService.setJobError(jobId, e.getMessage());
                }
            }).start();

            response.put("success", true);
            response.put("jobId", jobId);
            response.put("message", "🚀 Đang xử lý video tối ưu...");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get job status and progress
     */
    @GetMapping("/status/{jobId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getJobStatus(@PathVariable String jobId) {
        try {
            VideoReupJob job = reupJobService.getJob(jobId);
            Map<String, Object> response = new HashMap<>();

            if (job == null) {
                response.put("success", false);
                response.put("error", "Job không tìm thấy");
                return ResponseEntity.badRequest().body(response);
            }

            response.put("success", true);
            response.put("jobId", jobId);
            response.put("status", job.getStatus().toString());
            response.put("progress", job.getProgressPercent());

            if (job.getStatus() == VideoReupJob.JobStatus.COMPLETED) {
                // TRẢ VỀ CHỈ 1 VIDEO DUY NHẤT CHO GIAO DIỆN
                String outputPath = (job.getOutputVideoPaths() != null && !job.getOutputVideoPaths().isEmpty()) 
                                    ? job.getOutputVideoPaths().get(0) : null;
                response.put("outputPath", outputPath);
                response.put("message", "✅ Hoàn tất!");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Download output video (Single)
     */
    @GetMapping("/download/{jobId}")
    public ResponseEntity<byte[]> downloadVideo(@PathVariable String jobId) {
        try {
            VideoReupJob job = reupJobService.getJob(jobId);
            if (job == null || job.getOutputVideoPaths() == null || job.getOutputVideoPaths().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Lấy video đầu tiên (và duy nhất)
            Path videoPath = Paths.get("src/main/resources/static" + job.getOutputVideoPaths().get(0));
            if (!Files.exists(videoPath)) {
                // Fallback nếu path lưu dạng absolute
                videoPath = Paths.get(job.getOutputVideoPaths().get(0));
            }

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
