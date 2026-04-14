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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * Upload videos for bulk re-up processing
     */
    @PostMapping("/bulk-upload")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> bulkUploadForAdvancedReup(
            @RequestParam("videos") MultipartFile[] videos,
            @RequestParam(value = "textHook", required = false, defaultValue = "") String textHook,
            @RequestParam(value = "frameType", required = false, defaultValue = "none") String frameType,
            @RequestParam(value = "trendingMusic", required = false, defaultValue = "") String trendingMusicPath,
            @RequestParam(value = "includeVoiceover", required = false, defaultValue = "false") boolean includeVoiceover) {

        try {
            Map<String, Object> response = new HashMap<>();
            List<String> jobIds = new ArrayList<>();

            if (videos == null || videos.length == 0) {
                response.put("success", false);
                response.put("error", "Vui lòng chọn ít nhất một video");
                return ResponseEntity.badRequest().body(response);
            }

            Files.createDirectories(TEMP_UPLOAD_DIR);

            for (MultipartFile video : videos) {
                if (video.isEmpty()) continue;

                String originalName = video.getOriginalFilename();
                if (!isValidVideoFormat(originalName)) continue;

                String fileName = System.currentTimeMillis() + "_" + originalName;
                Path uploadedFilePath = TEMP_UPLOAD_DIR.resolve(fileName);
                video.transferTo(uploadedFilePath);

                VideoReupJob job = reupJobService.createJob(originalName, uploadedFilePath.toString());
                job.setSubtitleText(textHook);
                job.setFilterType(frameType); // Store frameType in filterType field
                
                reupJobService.updateJobStatus(job.getJobId(), VideoReupJob.JobStatus.QUEUED);
                jobIds.add(job.getJobId());
            }

            response.put("success", true);
            response.put("jobIds", jobIds);
            response.put("message", "✅ " + jobIds.size() + " video đã được tải lên. Nhấn 'Xử lý' để bắt đầu.");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LOG.severe("❌ Bulk upload error: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Lỗi upload: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Start processing a specific job
     */
    @PostMapping("/process/{jobId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processJob(
            @PathVariable String jobId,
            @RequestParam(value = "textHook", required = false, defaultValue = "") String textHook,
            @RequestParam(value = "frameType", required = false, defaultValue = "none") String frameType,
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

            new Thread(() -> {
                try {
                    Path inputPath = Paths.get(job.getFilePath());
                    String outputPath = advancedReupEngine.processAdvancedReup(
                        job, inputPath, textHook, trendingMusicPath, includeVoiceover, frameType
                    );
                    
                    List<String> outputs = new ArrayList<>();
                    outputs.add(outputPath);
                    reupJobService.completeJob(jobId, outputs);
                } catch (Exception e) {
                    LOG.severe("Error in processing job " + jobId + ": " + e.getMessage());
                    reupJobService.setJobError(jobId, e.getMessage());
                }
            }).start();

            response.put("success", true);
            response.put("message", "🚀 Đang xử lý...");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/status/{jobId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStatus(@PathVariable String jobId) {
        VideoReupJob job = reupJobService.getJob(jobId);
        Map<String, Object> response = new HashMap<>();
        
        if (job == null) {
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }

        response.put("success", true);
        response.put("status", job.getStatus().toString());
        response.put("progress", job.getProgressPercent());
        response.put("fileName", job.getFileName());
        
        if (job.getStatus() == VideoReupJob.JobStatus.COMPLETED) {
            response.put("outputVideo", job.getOutputVideoPaths().get(0));
        }
        
        return ResponseEntity.ok(response);
    }

    private boolean isValidVideoFormat(String fileName) {
        if (fileName == null) return false;
        String lower = fileName.toLowerCase();
        return lower.endsWith(".mp4") || lower.endsWith(".mov") || lower.endsWith(".avi");
    }
}
