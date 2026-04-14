package com.ninhquachhai.tiktoktool.model;

public class AiVideoJob {
    public enum Status { PENDING, PROCESSING, COMPLETED, FAILED }

    private String jobId;
    private Status status;
    private String videoUrl;
    private String errorMessage;
    private int videoIndex;

    public AiVideoJob(String jobId, int videoIndex) {
        this.jobId = jobId;
        this.videoIndex = videoIndex;
        this.status = Status.PENDING;
    }

    public String getJobId()            { return jobId; }
    public Status getStatus()           { return status; }
    public String getVideoUrl()         { return videoUrl; }
    public String getErrorMessage()     { return errorMessage; }
    public int getVideoIndex()          { return videoIndex; }

    public void setStatus(Status status)            { this.status = status; }
    public void setVideoUrl(String videoUrl)        { this.videoUrl = videoUrl; }
    public void setErrorMessage(String msg)         { this.errorMessage = msg; }
}
