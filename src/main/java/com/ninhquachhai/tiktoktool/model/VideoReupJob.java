package com.ninhquachhai.tiktoktool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a video re-up job with tracking information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoReupJob {
    private String jobId;
    private String fileName;
    private String filePath;
    private JobStatus status; // UPLOADING, QUEUED, PROCESSING, COMPLETED, ERROR
    private int progressPercent;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    // Re-up options
    private boolean applyMirror;
    private boolean applyCrop;
    private double speedMultiplier;
    private boolean addSubtitle;
    private String subtitleText;
    private boolean addFilter;
    private String filterType; // grayscale, sepia, blur, etc.
    private boolean addWatermark;
    private String watermarkText;

    // Output files
    private List<String> outputVideoPaths;

    public enum JobStatus {
        UPLOADING, QUEUED, PROCESSING, COMPLETED, ERROR
    }
}

