package com.ninhquachhai.tiktoktool.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for aspect ratio transformations (zoom, mirror, flip)
 */
@Service
public class AspectRatioTransformService {

    private static final Logger LOG = Logger.getLogger(AspectRatioTransformService.class.getName());
    private static final String FFMPEG_PATH = "C:\\ffmpeg\\ffmpeg.exe";

    /**
     * Apply aspect ratio transformation: zoom, mirror, or flip
     * @param inputVideo Input video path
     * @param outputVideo Output video path
     * @param zoomLevel Zoom level (0.8 = zoom out 20%, 1.2 = zoom in 20%)
     * @param isMirror If true, mirror (flip horizontally) the video
     * @param isVerticalFlip If true, flip vertically
     * @return FFmpeg command for this transformation
     */
    public List<String> buildZoomMirrorCommand(Path inputVideo, Path outputVideo,
                                               double zoomLevel, boolean isMirror,
                                               boolean isVerticalFlip) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        // Build filter chain
        StringBuilder filterChain = new StringBuilder();

        // Zoom transformation (using scale filter)
        if (zoomLevel != 1.0) {
            filterChain.append("scale=trunc(iw*").append(zoomLevel).append("/2)*2:")
                      .append("trunc(ih*").append(zoomLevel).append("/2)*2)");
        }

        // Mirror (hflip) and vertical flip (vflip)
        if (isMirror) {
            if (filterChain.length() > 0) filterChain.append(",");
            filterChain.append("hflip");
        }

        if (isVerticalFlip) {
            if (filterChain.length() > 0) filterChain.append(",");
            filterChain.append("vflip");
        }

        if (filterChain.length() > 0) {
            cmd.add("-vf");
            cmd.add(filterChain.toString());
        }

        cmd.add(outputVideo.toString());
        return cmd;
    }

    /**
     * Apply random zoom with small variations
     */
    public List<String> buildRandomZoomCommand(Path inputVideo, Path outputVideo) {
        double randomZoom = 0.95 + Math.random() * 0.1; // 0.95 to 1.05
        return buildZoomMirrorCommand(inputVideo, outputVideo, randomZoom, false, false);
    }

    /**
     * Apply mirror transformation
     */
    public List<String> buildMirrorCommand(Path inputVideo, Path outputVideo) {
        return buildZoomMirrorCommand(inputVideo, outputVideo, 1.0, true, false);
    }
}

