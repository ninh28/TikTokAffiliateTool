package com.ninhquachhai.tiktoktool.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for filter and effects transformations (color adjustments, effects)
 */
@Service
public class FilterEffectsService {

    private static final Logger LOG = Logger.getLogger(FilterEffectsService.class.getName());
    private static final String FFMPEG_PATH = "C:\\ffmpeg\\ffmpeg.exe";

    /**
     * Apply color grading/adjustment filters
     * @param inputVideo Input video path
     * @param outputVideo Output video path
     * @param brightness Brightness adjustment (-1 to 1, 0 = no change)
     * @param contrast Contrast adjustment (0.5 to 2, 1 = no change)
     * @param saturation Saturation adjustment (0 to 2, 1 = no change)
     * @return FFmpeg command for color adjustment
     */
    public List<String> buildColorAdjustCommand(Path inputVideo, Path outputVideo,
                                               double brightness, double contrast, double saturation) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        String filterChain = String.format(
            "eq=brightness=%.2f:contrast=%.2f:saturation=%.2f",
            brightness, contrast, saturation
        );

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Apply grayscale filter
     */
    public List<String> buildGrayscaleCommand(Path inputVideo, Path outputVideo) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");
        cmd.add("-vf");
        cmd.add("format=gray");
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Apply sepia tone filter
     */
    public List<String> buildSepiaCommand(Path inputVideo, Path outputVideo) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");
        cmd.add("-vf");
        cmd.add("colorchannelmixer=.393:.769:.189:0:.349:.686:.168:0:.272:.534:.131");
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Apply brightness boost
     */
    public List<String> buildBrightnessBoostCommand(Path inputVideo, Path outputVideo) {
        return buildColorAdjustCommand(inputVideo, outputVideo, 0.15, 1.1, 1.05);
    }

    /**
     * Apply vibrant/saturated look
     */
    public List<String> buildVibrantCommand(Path inputVideo, Path outputVideo) {
        return buildColorAdjustCommand(inputVideo, outputVideo, 0.0, 1.2, 1.3);
    }

    /**
     * Apply blur effect
     */
    public List<String> buildBlurCommand(Path inputVideo, Path outputVideo, int blurRadius) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");
        cmd.add("-vf");
        cmd.add("boxblur=" + blurRadius);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Apply edge detection filter
     */
    public List<String> buildEdgeDetectCommand(Path inputVideo, Path outputVideo) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");
        cmd.add("-vf");
        cmd.add("edgedetect=sobel");
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Combine multiple filters into one command
     */
    public List<String> buildCombinedFilterCommand(Path inputVideo, Path outputVideo, String... filters) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");
        cmd.add("-vf");
        cmd.add(String.join(",", filters));
        cmd.add(outputVideo.toString());

        return cmd;
    }
}

