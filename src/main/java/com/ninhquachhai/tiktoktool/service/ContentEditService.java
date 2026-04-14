package com.ninhquachhai.tiktoktool.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service for content editing transformations (crop, rearrange scenes)
 */
@Service
public class ContentEditService {

    private static final Logger LOG = Logger.getLogger(ContentEditService.class.getName());
    private static final String FFMPEG_PATH = "C:\\ffmpeg\\ffmpeg.exe";

    /**
     * Crop video with random position variations
     * @param inputVideo Input video path
     * @param outputVideo Output video path
     * @param cropWidth Width of crop area (percentage, 0.8 = 80% of original)
     * @param cropHeight Height of crop area (percentage)
     * @return FFmpeg command for cropping
     */
    public List<String> buildRandomCropCommand(Path inputVideo, Path outputVideo,
                                               double cropWidth, double cropHeight) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        // Random crop positions
        String filterChain = String.format(
            "crop=trunc(iw*%.2f):trunc(ih*%.2f):trunc((iw-iw*%.2f)/2):trunc((ih-ih*%.2f)/2)",
            cropWidth, cropHeight, cropWidth, cropHeight
        );

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Trim/cut video from start time to end time
     * @param inputVideo Input video path
     * @param outputVideo Output video path
     * @param startTime Start time in seconds
     * @param duration Duration in seconds (0 or negative = till end)
     * @return FFmpeg command for trimming
     */
    public List<String> buildTrimCommand(Path inputVideo, Path outputVideo,
                                        double startTime, double duration) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-ss");
        cmd.add(String.valueOf(startTime));

        if (duration > 0) {
            cmd.add("-t");
            cmd.add(String.valueOf(duration));
        }

        cmd.add("-y");
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Speed up video (useful for trimming multiple sections and playing faster)
     * @param inputVideo Input video path
     * @param outputVideo Output video path
     * @param speedFactor Speed factor (1.1 = 10% faster, 1.5 = 50% faster)
     * @return FFmpeg command for speed adjustment
     */
    public List<String> buildSpeedCommand(Path inputVideo, Path outputVideo, double speedFactor) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");

        String filterChain = String.format("setpts=PTS/%.2f", speedFactor);

        cmd.add("-vf");
        cmd.add(filterChain);
        cmd.add("-af");
        cmd.add(String.format("atempo=%.2f", speedFactor));
        cmd.add(outputVideo.toString());

        return cmd;
    }

    /**
     * Rearrange video by cutting into segments and reversing order (complex editing)
     */
    public List<String> buildReverseCommand(Path inputVideo, Path outputVideo) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-y");
        cmd.add("-vf");
        cmd.add("reverse");
        cmd.add(outputVideo.toString());

        return cmd;
    }
}

