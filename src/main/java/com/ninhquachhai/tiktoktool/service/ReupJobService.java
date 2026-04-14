package com.ninhquachhai.tiktoktool.service;

import com.ninhquachhai.tiktoktool.model.VideoReupJob;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Service for managing video re-up jobs
 */
@Service
public class ReupJobService {

    private static final Logger LOG = Logger.getLogger(ReupJobService.class.getName());

    // In-memory job store (consider adding persistent DB in future)
    private final Map<String, VideoReupJob> jobStore = new HashMap<>();

    private final ReupVariationEngine variationEngine;

    public ReupJobService(ReupVariationEngine variationEngine) {
        this.variationEngine = variationEngine;
    }

    /**
     * Create a new re-up job
     */
    public VideoReupJob createJob(String fileName, String filePath) {
        String jobId = UUID.randomUUID().toString();

        VideoReupJob job = new VideoReupJob();
        job.setJobId(jobId);
        job.setFileName(fileName);
        job.setFilePath(filePath);
        job.setStatus(VideoReupJob.JobStatus.UPLOADING);
        job.setProgressPercent(0);
        job.setCreatedAt(LocalDateTime.now());

        jobStore.put(jobId, job);
        LOG.info("📋 Created new job: " + jobId);

        return job;
    }

    /**
     * Get job by ID
     */
    public VideoReupJob getJob(String jobId) {
        return jobStore.get(jobId);
    }

    /**
     * Update job status
     */
    public void updateJobStatus(String jobId, VideoReupJob.JobStatus status) {
        VideoReupJob job = jobStore.get(jobId);
        if (job != null) {
            job.setStatus(status);
            LOG.info("📊 Job " + jobId + " status updated to: " + status);
        }
    }

    /**
     * Update job progress
     */
    public void updateJobProgress(String jobId, int progressPercent) {
        VideoReupJob job = jobStore.get(jobId);
        if (job != null) {
            job.setProgressPercent(progressPercent);
            LOG.info("⏳ Job " + jobId + " progress: " + progressPercent + "%");
        }
    }

    /**
     * Set job error
     */
    public void setJobError(String jobId, String errorMessage) {
        VideoReupJob job = jobStore.get(jobId);
        if (job != null) {
            job.setStatus(VideoReupJob.JobStatus.ERROR);
            job.setErrorMessage(errorMessage);
            LOG.severe("❌ Job " + jobId + " error: " + errorMessage);
        }
    }

    /**
     * Complete job with output videos
     */
    public void completeJob(String jobId, List<String> outputVideoPaths) {
        VideoReupJob job = jobStore.get(jobId);
        if (job != null) {
            job.setStatus(VideoReupJob.JobStatus.COMPLETED);
            job.setProgressPercent(100);
            job.setOutputVideoPaths(outputVideoPaths);
            job.setCompletedAt(LocalDateTime.now());
            LOG.info("✅ Job " + jobId + " completed with " + outputVideoPaths.size() + " outputs");
        }
    }

    /**
     * Process re-up job
     */
    public void processReupJob(String jobId, Path inputVideoPath) {
        try {
            VideoReupJob job = jobStore.get(jobId);
            if (job == null) {
                throw new RuntimeException("Job not found: " + jobId);
            }

            updateJobStatus(jobId, VideoReupJob.JobStatus.PROCESSING);
            updateJobProgress(jobId, 10);

            LOG.info("🚀 Starting processing for job: " + jobId);

            // Generate 3 variations
            List<String> outputs = variationEngine.generateVariations(job, inputVideoPath);

            updateJobProgress(jobId, 90);

            // Complete job
            completeJob(jobId, outputs);

            LOG.info("🎉 Job " + jobId + " completed successfully!");

        } catch (Exception e) {
            LOG.severe("Error processing job " + jobId + ": " + e.getMessage());
            e.printStackTrace();
            setJobError(jobId, e.getMessage());
        }
    }
}

