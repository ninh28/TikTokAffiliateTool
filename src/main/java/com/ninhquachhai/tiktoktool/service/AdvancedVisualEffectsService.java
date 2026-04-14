package com.ninhquachhai.tiktoktool.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Advanced Visual Effects Service
 * Handles zoom, trim, flip, color grading, noise overlay, and visual hooks
 */
@Service
public class AdvancedVisualEffectsService {

    private static final Logger LOG = Logger.getLogger(AdvancedVisualEffectsService.class.getName());
    private static final String FFMPEG_PATH = "C:\\ffmpeg\\ffmpeg.exe";
    private static final String FONT_PATH = "C:/Windows/Fonts/arial.ttf";

    /**
     * Trim video - remove first and last N seconds
     * @param inputVideo Input video
     * @param outputVideo Output video
     * @param trimStart Seconds to trim from start
     * @param trimEnd Seconds to trim from end (will be calculated from duration)
     */
    public List<String> buildTrimCommand(Path inputVideo, Path outputVideo, double trimStart, double trimEnd) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-ss");
        cmd.add(String.valueOf(trimStart));

        if (trimEnd > 0) {
            // Use -to for end time instead of -t
            cmd.add("-to");
            cmd.add("duration=" + trimEnd); // Will be calculated
        }

        cmd.add("-y");
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Zoom video 1.1x and crop to remove edges
     * Effectively: video gets bigger, old edges disappear
     */
    public List<String> buildZoomAndCropCommand(Path inputVideo, Path outputVideo, double zoomLevel) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        // Zoom with crop: scale up then crop center
        String filterChain = String.format(
            "scale=iw*%.2f:ih*%.2f,crop=iw/%.2f:ih/%.2f",
            zoomLevel, zoomLevel, zoomLevel, zoomLevel
        );

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Flip video horizontally (mirror)
     */
    public List<String> buildFlipHorizontalCommand(Path inputVideo, Path outputVideo) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");
        cmd.add("-vf");
        cmd.add("hflip");
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Advanced color grading: Temperature + Contrast + Saturation
     * @param inputVideo Input video
     * @param outputVideo Output video
     * @param temperature Warm (0.8) to Cool (1.2), 1.0 = neutral
     * @param contrast +5 means 1.05
     * @param saturation -3% means 0.97
     */
    public List<String> buildAdvancedColorGradingCommand(Path inputVideo, Path outputVideo,
                                                         double temperature, double contrast, double saturation) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        // Build filter chain
        StringBuilder filterChain = new StringBuilder();

        // Temperature: Warm (red) or Cool (blue)
        if (temperature != 1.0) {
            if (temperature < 1.0) {
                // Cool tone: reduce red, increase blue
                filterChain.append(String.format("colortemperature=%.2f", 3000 + (temperature * 2000)));
            } else {
                // Warm tone: increase red
                filterChain.append(String.format("colortemperature=%.2f", 3000 + (temperature * 2000)));
            }
        }

        // Contrast and saturation
        if (filterChain.length() > 0) filterChain.append(",");
        filterChain.append(String.format("eq=contrast=%.2f:saturation=%.2f", contrast, saturation));

        cmd.add("-vf");
        cmd.add(filterChain.toString());
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Add text hook at beginning (3 seconds)
     * Bold text with contrasting background
     */
    public List<String> buildTextHookCommand(Path inputVideo, Path outputVideo,
                                            String hookText, double duration) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        String escapedText = escapeText(hookText);

        // Draw bold text with box background
        String filterChain = String.format(
            "drawtext=fontfile='%s':text='%s':fontsize=48:fontweight=bold:" +
            "fontcolor=FFFFFF:x=(w-text_w)/2:y=(h-text_h)/2:" +
            "boxcolor=000000:boxborderw=5:enable='lt(t\\,%.1f)'",
            FONT_PATH, escapedText, duration
        );

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Add grain/noise overlay for algorithm bypass
     * Grain at 3% opacity
     */
    public List<String> buildGrainOverlayCommand(Path inputVideo, Path outputVideo) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        // Add noise grain effect
        // Using rgbnoise for subtle grain at very low opacity
        String filterChain = "rgbnoise=c0=.1:c1=.05:c2=.05";

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Combine all visual effects in one filter chain
     */
    public List<String> buildCombinedVisualEffectsCommand(Path inputVideo, Path outputVideo,
                                                          String textHook, double zoomLevel,
                                                          double temperature, double contrast, double saturation) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        StringBuilder filterChain = new StringBuilder();

        // 1. Zoom and crop
        filterChain.append(String.format(
            "scale=iw*%.2f:ih*%.2f,crop=iw/%.2f:ih/%.2f",
            zoomLevel, zoomLevel, zoomLevel, zoomLevel
        ));

        // 2. Color grading
        filterChain.append(String.format(
            ",eq=contrast=%.2f:saturation=%.2f",
            contrast, saturation
        ));

        // 3. Flip horizontal
        filterChain.append(",hflip");

        // 4. Text hook (first 3 seconds)
        String escapedText = escapeText(textHook);
        filterChain.append(String.format(
            ",drawtext=fontfile='%s':text='%s':fontsize=48:fontweight=bold:" +
            "fontcolor=FFFFFF:x=(w-text_w)/2:y=(h-text_h)/2:" +
            "boxcolor=000000:boxborderw=5:enable='lt(t\\,3)'",
            FONT_PATH, escapedText
        ));

        // 5. Grain overlay
        filterChain.append(",rgbnoise=c0=.1:c1=.05:c2=.05");

        cmd.add("-vf");
        cmd.add(filterChain.toString());
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Escape text for FFmpeg drawtext filter
     */
    private String escapeText(String text) {
        if (text == null) return "";
        return text.replace("'", "\\'")
                   .replace("\\", "\\\\")
                   .replace(":", "\\:")
                   .replace("\n", "\\n");
    }

    /**
     * Speed up video (1.1x = 10% faster)
     * Applied with corresponding audio tempo change
     */
    public List<String> buildSpeedUpCommand(Path inputVideo, Path outputVideo, double speedFactor) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        // Video filter for speed
        String videoFilter = String.format("setpts=PTS/%.2f", speedFactor);

        // Audio filter for speed (preserve pitch)
        String audioFilter = String.format("atempo=%.2f", speedFactor);

        cmd.add("-vf");
        cmd.add(videoFilter);
        cmd.add("-af");
        cmd.add(audioFilter);
        cmd.add(outputVideo.toString());

        return cmd;
    }
}

