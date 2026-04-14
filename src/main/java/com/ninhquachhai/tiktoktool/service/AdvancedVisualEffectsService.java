package com.ninhquachhai.tiktoktool.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Advanced Visual Effects Service
 * Handles zoom, trim, flip, color grading, noise overlay, framing and visual hooks
 */
@Service
public class AdvancedVisualEffectsService {

    private static final Logger LOG = Logger.getLogger(AdvancedVisualEffectsService.class.getName());
    private static final String FFMPEG_PATH = "C:\\ffmpeg\\ffmpeg.exe";
    private static final String FONT_PATH = "C:/Windows/Fonts/arial.ttf";

    /**
     * Trim video - remove first and last N seconds
     */
    public List<String> buildTrimCommand(Path inputVideo, Path outputVideo, double trimStart, double trimEnd) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-ss");
        cmd.add(String.valueOf(trimStart));

        if (trimEnd > 0) {
            cmd.add("-to");
            cmd.add("duration=" + trimEnd);
        }

        cmd.add("-y");
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Apply professional frames to video
     * @param inputVideo Input path
     * @param outputVideo Output path
     * @param frameType "news", "phone", "review", or "none"
     */
    public List<String> buildFrameCommand(Path inputVideo, Path outputVideo, String frameType) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        String filterChain = "";
        switch (frameType.toLowerCase()) {
            case "news":
                // Top and bottom black bars with "TIN TỨC MỚI" text
                filterChain = "pad=iw:ih+160:0:80:black," +
                             "drawtext=fontfile='" + FONT_PATH + "':text='TIN TỨC MỚI NHẤT':fontcolor=white:fontsize=40:x=(w-text_w)/2:y=20," +
                             "drawtext=fontfile='" + FONT_PATH + "':text='XEM NGAY ĐỂ BIẾT CHI TIẾT':fontcolor=yellow:fontsize=30:x=(w-text_w)/2:y=h-50";
                break;
            case "review":
                // White frame with "REVIEW CHÂN THỰC"
                filterChain = "pad=iw+40:ih+160:20:80:white," +
                             "drawtext=fontfile='" + FONT_PATH + "':text='REVIEW CHÂN THỰC':fontcolor=black:fontsize=45:fontweight=bold:x=(w-text_w)/2:y=15," +
                             "drawtext=fontfile='" + FONT_PATH + "':text='GIÁ CỰC SỐC TẠI GIỎ HÀNG ⬇️':fontcolor=red:fontsize=32:x=(w-text_w)/2:y=h-55";
                break;
            case "phone":
                // Simulated phone frame (rounded corners and border)
                filterChain = "pad=iw+60:ih+120:30:60:black," +
                             "drawbox=c=gray:t=5:x=25:y=55:w=iw-50:h=ih-110";
                break;
            default:
                filterChain = "copy";
                break;
        }

        if (!filterChain.equals("copy")) {
            cmd.add("-vf");
            cmd.add(filterChain);
        }
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Combined visual effects with frame support
     */
    public List<String> buildCombinedVisualEffectsCommand(Path inputVideo, Path outputVideo,
                                                          String textHook, String frameType,
                                                          double zoomLevel, double temperature, 
                                                          double contrast, double saturation) {
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

        // 4. Framing
        if (frameType != null && !frameType.equals("none")) {
            switch (frameType.toLowerCase()) {
                case "news":
                    filterChain.append(",pad=iw:ih+160:0:80:black" +
                        ",drawtext=fontfile='" + FONT_PATH + "':text='TIN TỨC MỚI NHẤT':fontcolor=white:fontsize=40:x=(w-text_w)/2:y=20" +
                        ",drawtext=fontfile='" + FONT_PATH + "':text='XEM NGAY ĐỂ BIẾT CHI TIẾT':fontcolor=yellow:fontsize=30:x=(w-text_w)/2:y=h-50");
                    break;
                case "review":
                    filterChain.append(",pad=iw+40:ih+160:20:80:white" +
                        ",drawtext=fontfile='" + FONT_PATH + "':text='REVIEW CHÂN THỰC':fontcolor=black:fontsize=45:x=(w-text_w)/2:y=15" +
                        ",drawtext=fontfile='" + FONT_PATH + "':text='SĂN DEAL NGAY TRONG GIỎ HÀNG ⬇️':fontcolor=red:fontsize=32:x=(w-text_w)/2:y=h-55");
                    break;
            }
        }

        // 5. Text hook (first 3 seconds)
        if (textHook != null && !textHook.isEmpty()) {
            String escapedText = escapeText(textHook);
            filterChain.append(String.format(
                ",drawtext=fontfile='%s':text='%s':fontsize=48:fontweight=bold:" +
                "fontcolor=FFFFFF:x=(w-text_w)/2:y=(h-text_h)/2:" +
                "boxcolor=000000:boxborderw=5:enable='lt(t\\,3)'",
                FONT_PATH, escapedText
            ));
        }

        // 6. Grain overlay
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

    public List<String> buildSpeedUpCommand(Path inputVideo, Path outputVideo, double speedFactor) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");
        cmd.add("-vf");
        cmd.add(String.format("setpts=PTS/%.2f", speedFactor));
        cmd.add("-af");
        cmd.add(String.format("atempo=%.2f", speedFactor));
        cmd.add(outputVideo.toString());
        return cmd;
    }
}
