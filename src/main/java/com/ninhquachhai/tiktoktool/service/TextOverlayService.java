package com.ninhquachhai.tiktoktool.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for text overlay and sticker transformations (watermarks, timestamps, text)
 */
@Service
public class TextOverlayService {

    private static final Logger LOG = Logger.getLogger(TextOverlayService.class.getName());
    private static final String FFMPEG_PATH = "C:\\ffmpeg\\ffmpeg.exe";
    private static final String FONT_PATH = "C:/Windows/Fonts/arial.ttf";

    /**
     * Add text watermark to video
     * @param inputVideo Input video path
     * @param outputVideo Output video path
     * @param text Text to overlay
     * @param positionX X position (x or 'w-text_w-10' format)
     * @param positionY Y position (y or 'h-text_h-10' format)
     * @param fontSize Font size
     * @param fontColor Font color (hex format like 'FFFFFF')
     * @return FFmpeg command for text overlay
     */
    public List<String> buildTextOverlayCommand(Path inputVideo, Path outputVideo,
                                               String text, String positionX, String positionY,
                                               int fontSize, String fontColor) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        // Escape text for FFmpeg
        String escapedText = escapeText(text);

        String filterChain = String.format(
            "drawtext=fontfile='%s':text='%s':fontsize=%d:fontcolor=%s:x=%s:y=%s",
            FONT_PATH, escapedText, fontSize, fontColor, positionX, positionY
        );

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Add text at top-left corner (common watermark position)
     */
    public List<String> buildTopLeftWatermarkCommand(Path inputVideo, Path outputVideo,
                                                     String text, int fontSize) {
        return buildTextOverlayCommand(inputVideo, outputVideo, text, "10", "10", fontSize, "FFFFFF");
    }

    /**
     * Add text at bottom-right corner (common watermark position)
     */
    public List<String> buildBottomRightWatermarkCommand(Path inputVideo, Path outputVideo,
                                                         String text, int fontSize) {
        return buildTextOverlayCommand(inputVideo, outputVideo, text,
            "w-text_w-10", "h-text_h-10", fontSize, "FFFFFF");
    }

    /**
     * Add text at bottom-center (common caption position)
     */
    public List<String> buildCenterBottomCaptionCommand(Path inputVideo, Path outputVideo,
                                                        String text, int fontSize) {
        return buildTextOverlayCommand(inputVideo, outputVideo, text,
            "(w-text_w)/2", "h-text_h-10", fontSize, "FFFFFF");
    }

    /**
     * Add timestamp to video (shows current time during playback)
     */
    public List<String> buildTimestampCommand(Path inputVideo, Path outputVideo, int fontSize) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        String filterChain = String.format(
            "drawtext=fontfile='%s':text='%%{pts\\:hms}':fontsize=%d:fontcolor=FFFFFF:x=10:y=10",
            FONT_PATH, fontSize
        );

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Add decorative border/frame around video (using drawbox)
     */
    public List<String> buildBorderCommand(Path inputVideo, Path outputVideo,
                                          String borderColor, int borderWidth) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        String filterChain = String.format(
            "drawbox=color=%s:thickness=%d:x=0:y=0:w=iw:h=ih",
            borderColor, borderWidth
        );

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Add multiple text overlays (watermark + caption)
     */
    public List<String> buildMultiTextCommand(Path inputVideo, Path outputVideo,
                                             String watermark, String caption) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        String escapedWatermark = escapeText(watermark);
        String escapedCaption = escapeText(caption);

        String filterChain = String.format(
            "drawtext=fontfile='%s':text='%s':fontsize=24:fontcolor=FFFFFF:x=10:y=10," +
            "drawtext=fontfile='%s':text='%s':fontsize=28:fontcolor=FFFFFF:x=(w-text_w)/2:y=h-text_h-10",
            FONT_PATH, escapedWatermark, FONT_PATH, escapedCaption
        );

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Escape special characters for FFmpeg drawtext filter
     */
    private String escapeText(String text) {
        if (text == null) return "";
        return text.replace("'", "\\'")
                   .replace("\\", "\\\\")
                   .replace(":", "\\:");
    }
}

