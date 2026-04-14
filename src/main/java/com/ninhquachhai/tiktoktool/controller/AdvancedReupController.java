package com.ninhquachhai.tiktoktool.controller;

import com.ninhquachhai.tiktoktool.model.VideoReupJob;
import com.ninhquachhai.tiktoktool.service.ReupJobService;
import com.ninhquachhai.tiktoktool.service.AdvancedReupEngine;
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
 * Advanced Re-up Controller (Flow Cắt Lại)
 * Handles the complete advanced re-up workflow with maximum originality
 */
@Controller
@RequestMapping("/advanced-reup")
public class AdvancedReupController {

    private static final Logger LOG = Logger.getLogger(AdvancedReupController.class.getName());
    private static final Path TEMP_UPLOAD_DIR = Paths.get("temp_uploads/advanced");

    private final ReupJobService reupJobService;
    private final AdvancedReupEngine advancedReupEngine;

    public AdvancedReupController(ReupJobService reupJobService, AdvancedReupEngine advancedReupEngine) {
        this.reupJobService = reupJobService;
        this.advancedReupEngine = advancedReupEngine;
    }

    /**
     * Display advanced re-up page
     */
    @GetMapping
    public String advancedReupPage() {
        return "advanced_reup";
    }

    /**
     * Upload video for advanced re-up processing
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadForAdvancedReup(
            @RequestParam("video") MultipartFile video,
            @RequestParam(value = "textHook", required = false, defaultValue = "") String textHook,
            @RequestParam(value = "trendingMusic", required = false, defaultValue = "") String trendingMusicPath,
            @RequestParam(value = "includeVoiceover", required = false, defaultValue = "false") boolean includeVoiceover) {

        try {
            Map<String, Object> response = new HashMap<>();

            // Validate file
            if (video == null || video.isEmpty()) {
                response.put("success", false);
                response.put("error", "Vui lòng chọn video để upload");
                return ResponseEntity.badRequest().body(response);
            }

            String originalName = video.getOriginalFilename();
            if (!isValidVideoFormat(originalName)) {
                response.put("success", false);
                response.put("error", "Định dạng video không hỗ trợ. Vui lòng upload MP4, MOV, AVI, MKV...");
                return ResponseEntity.badRequest().body(response);
            }

            // Create temp upload directory
            Files.createDirectories(TEMP_UPLOAD_DIR);

            // Save uploaded file
            String fileName = System.currentTimeMillis() + "_" + originalName;
            Path uploadedFilePath = TEMP_UPLOAD_DIR.resolve(fileName);
            video.transferTo(uploadedFilePath);

            LOG.info("📹 Advanced re-up video uploaded: " + fileName);
            LOG.info("📝 Text Hook: " + textHook);
            LOG.info("🎵 Trending Music: " + (trendingMusicPath.isEmpty() ? "Not provided" : trendingMusicPath));
            LOG.info("🎙️ Include Voiceover: " + includeVoiceover);

            // Create re-up job
            VideoReupJob job = reupJobService.createJob(originalName, uploadedFilePath.toString());
            job.setSubtitleText(textHook);  // Use subtitle field to store text hook
            job.setAddFilter(true);  // Color grading enabled
            job.setAddWatermark(true);  // Grain overlay (using watermark field)

            // Update job to QUEUED
            reupJobService.updateJobStatus(job.getJobId(), VideoReupJob.JobStatus.QUEUED);

            response.put("success", true);
            response.put("jobId", job.getJobId());
            response.put("message", "✅ Video uploaded successfully. Click 'Process' to start advanced re-up.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LOG.severe("❌ Upload error: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Lỗi upload: " + e.getMessage());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Start advanced re-up processing
     */
    @PostMapping("/process/{jobId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processAdvancedReup(
            @PathVariable String jobId,
            @RequestParam(value = "textHook", required = false, defaultValue = "") String textHook,
            @RequestParam(value = "trendingMusic", required = false, defaultValue = "") String trendingMusicPath,
            @RequestParam(value = "includeVoiceover", required = false, defaultValue = "false") boolean includeVoiceover) {

        try {
            VideoReupJob job = reupJobService.getJob(jobId);

            Map<String, Object> response = new HashMap<>();

            if (job == null) {
                response.put("success", false);
                response.put("error", "Job không tìm thấy");
                return ResponseEntity.badRequest().body(response);
            }

            if (job.getStatus() == VideoReupJob.JobStatus.PROCESSING) {
                response.put("success", false);
                response.put("error", "Job đang được xử lý, vui lòng chờ...");
                return ResponseEntity.badRequest().body(response);
            }

            // Start processing in a separate thread
            new Thread(() -> {
                try {
                    Path inputPath = Paths.get(job.getFilePath());
                    String outputPath = advancedReupEngine.processAdvancedReup(
                        job, inputPath, textHook, trendingMusicPath, includeVoiceover
                    );

                    // Update job with result
                    java.util.List<String> outputs = new java.util.ArrayList<>();
                    outputs.add(outputPath);
                    reupJobService.completeJob(jobId, outputs);

                } catch (Exception e) {
                    LOG.severe("Error in processing thread: " + e.getMessage());
                    e.printStackTrace();
                    reupJobService.setJobError(jobId, e.getMessage());
                }
            }).start();

            response.put("success", true);
            response.put("jobId", jobId);
            response.put("message", "🚀 Đang xử lý video với Flow Cắt Lại Advanced...");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LOG.severe("❌ Process error: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Lỗi xử lý: " + e.getMessage());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get job status and progress
     */
    @GetMapping("/status/{jobId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStatus(@PathVariable String jobId) {
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
            response.put("fileName", job.getFileName());

            if (job.getStatus() == VideoReupJob.JobStatus.COMPLETED) {
                response.put("outputVideo", job.getOutputVideoPaths() != null && !job.getOutputVideoPaths().isEmpty()
                    ? job.getOutputVideoPaths().get(0)
                    : null);
                response.put("message", "✅ Xử lý hoàn tất! Tải video ngay.");
            } else if (job.getStatus() == VideoReupJob.JobStatus.ERROR) {
                response.put("error", job.getErrorMessage());
                response.put("message", "❌ Lỗi: " + job.getErrorMessage());
            } else if (job.getStatus() == VideoReupJob.JobStatus.PROCESSING) {
                response.put("message", "⏳ Đang xử lý (" + job.getProgressPercent() + "%)...");
            } else {
                response.put("message", "📋 Chờ xử lý...");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LOG.severe("❌ Status check error: " + e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Lỗi kiểm tra trạng thái: " + e.getMessage());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Download output video
     */
    @GetMapping("/download/{jobId}")
    public ResponseEntity<byte[]> downloadOutput(@PathVariable String jobId) {
        try {
            VideoReupJob job = reupJobService.getJob(jobId);

            if (job == null || job.getOutputVideoPaths() == null || job.getOutputVideoPaths().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path videoPath = Paths.get(job.getOutputVideoPaths().get(0));

            if (!Files.exists(videoPath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] videoData = Files.readAllBytes(videoPath);

            String fileName = "advanced_reup_" + jobId + ".mp4";

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .header("Content-Type", "video/mp4")
                    .body(videoData);

        } catch (Exception e) {
            LOG.severe("❌ Download error: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Validate video format
     */
    private boolean isValidVideoFormat(String fileName) {
        if (fileName == null) return false;
        String lower = fileName.toLowerCase();
        return lower.endsWith(".mp4") || lower.endsWith(".mov") || lower.endsWith(".avi") ||
               lower.endsWith(".mkv") || lower.endsWith(".webm") || lower.endsWith(".flv") ||
               lower.endsWith(".m4v");
    }
}

