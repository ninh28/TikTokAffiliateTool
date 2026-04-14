package com.ninhquachhai.tiktoktool.service;

import com.ninhquachhai.tiktoktool.model.VideoReupJob;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Main orchestrator service for video re-up variations
 * Applies transformations and generates 3 distinct video variations
 */
@Service
public class ReupVariationEngine {

    private static final Logger LOG = Logger.getLogger(ReupVariationEngine.class.getName());
    private static final String OUTPUT_DIR = "src/main/resources/static/videos/reup/";

    private final AspectRatioTransformService aspectRatioService;
    private final ContentEditService contentEditService;
    private final FilterEffectsService filterService;
    private final TextOverlayService textService;

    public ReupVariationEngine(AspectRatioTransformService aspectRatioService,
                              ContentEditService contentEditService,
                              FilterEffectsService filterService,
                              TextOverlayService textService) {
        this.aspectRatioService = aspectRatioService;
        this.contentEditService = contentEditService;
        this.filterService = filterService;
        this.textService = textService;
    }

    /**
     * Generate 3 variations of re-upped video based on job configuration
     * @param job VideoReupJob with all transformation options
     * @param inputVideoPath Path to original uploaded video
     * @return List of 3 output video paths
     */
    public List<String> generateVariations(VideoReupJob job, Path inputVideoPath) throws Exception {
        Files.createDirectories(Paths.get(OUTPUT_DIR));

        List<String> outputPaths = new ArrayList<>();

        LOG.info("🎬 Starting re-up variation generation for job: " + job.getJobId());

        // Generate 3 variations with different transformation emphasis
        String v1Path = generateVariation1(job, inputVideoPath);
        String v2Path = generateVariation2(job, inputVideoPath);
        String v3Path = generateVariation3(job, inputVideoPath);

        outputPaths.add(v1Path);
        outputPaths.add(v2Path);
        outputPaths.add(v3Path);

        LOG.info("✅ All 3 variations generated successfully");
        return outputPaths;
    }

    /**
     * Variation 1: Aspect Ratio Focus (Mirror, Zoom, Light Speed adjustment)
     */
    private String generateVariation1(VideoReupJob job, Path inputVideoPath) throws Exception {
        Path outputPath = Paths.get(OUTPUT_DIR, job.getJobId() + "_v1.mp4");

        LOG.info("🎯 Generating Variation 1: Aspect Ratio Focus");

        List<String> ffmpegCmd = new ArrayList<>();
        ffmpegCmd.add("C:\\ffmpeg\\ffmpeg.exe");
        ffmpegCmd.add("-i");
        ffmpegCmd.add(inputVideoPath.toString());
        ffmpegCmd.add("-y");

        // Build filter chain for V1: Mirror + Zoom + Speed
        StringBuilder filterChain = new StringBuilder();

        // Apply zoom/mirror transformations
        if (job.isApplyMirror()) {
            filterChain.append("hflip");
        }

        if (job.isApplyCrop()) {
            if (filterChain.length() > 0) filterChain.append(",");
            filterChain.append("scale=trunc(iw*0.95/2)*2:trunc(ih*0.95/2)*2");
        }

        // Add subtle brightness for variation
        if (filterChain.length() > 0) filterChain.append(",");
        filterChain.append("eq=brightness=0.05:contrast=1.05");

        if (filterChain.length() > 0) {
            ffmpegCmd.add("-vf");
            ffmpegCmd.add(filterChain.toString());
        }

        // Apply speed
        if (job.getSpeedMultiplier() > 0 && job.getSpeedMultiplier() != 1.0) {
            ffmpegCmd.add("-filter:a");
            ffmpegCmd.add("atempo=" + job.getSpeedMultiplier());
        }

        ffmpegCmd.add(outputPath.toString());

        executeFFmpegCommand(ffmpegCmd);

        // Add watermark/subtitle if enabled
        if (job.isAddWatermark() || job.isAddSubtitle()) {
            Path tempPath = Paths.get(OUTPUT_DIR, job.getJobId() + "_v1_temp.mp4");
            Files.move(outputPath, tempPath);
            addTextOverlays(job, tempPath, outputPath);
        }

        LOG.info("✓ Variation 1 completed: " + outputPath);
        return outputPath.toString();
    }

    /**
     * Variation 2: Filter & Effects Focus (Color grading, Brightness, Saturation)
     */
    private String generateVariation2(VideoReupJob job, Path inputVideoPath) throws Exception {
        Path outputPath = Paths.get(OUTPUT_DIR, job.getJobId() + "_v2.mp4");

        LOG.info("🎯 Generating Variation 2: Filter & Effects Focus");

        List<String> ffmpegCmd = new ArrayList<>();
        ffmpegCmd.add("C:\\ffmpeg\\ffmpeg.exe");
        ffmpegCmd.add("-i");
        ffmpegCmd.add(inputVideoPath.toString());
        ffmpegCmd.add("-y");

        StringBuilder filterChain = new StringBuilder();

        // Apply vibrant color grading
        filterChain.append("eq=brightness=0.1:contrast=1.2:saturation=1.3");

        if (job.isApplyMirror()) {
            filterChain.append(",hflip");
        }

        if (job.isApplyCrop()) {
            filterChain.append(",crop=iw*0.9:ih*0.9:(iw-iw*0.9)/2:(ih-ih*0.9)/2");
        }

        ffmpegCmd.add("-vf");
        ffmpegCmd.add(filterChain.toString());

        if (job.getSpeedMultiplier() > 0 && job.getSpeedMultiplier() != 1.0) {
            ffmpegCmd.add("-filter:a");
            ffmpegCmd.add("atempo=" + job.getSpeedMultiplier());
        }

        ffmpegCmd.add(outputPath.toString());

        executeFFmpegCommand(ffmpegCmd);

        if (job.isAddWatermark() || job.isAddSubtitle()) {
            Path tempPath = Paths.get(OUTPUT_DIR, job.getJobId() + "_v2_temp.mp4");
            Files.move(outputPath, tempPath);
            addTextOverlays(job, tempPath, outputPath);
        }

        LOG.info("✓ Variation 2 completed: " + outputPath);
        return outputPath.toString();
    }

    /**
     * Variation 3: Content Edit & Text Focus (Trim, Speed, Heavy text/watermark)
     */
    private String generateVariation3(VideoReupJob job, Path inputVideoPath) throws Exception {
        Path outputPath = Paths.get(OUTPUT_DIR, job.getJobId() + "_v3.mp4");

        LOG.info("🎯 Generating Variation 3: Content Edit & Text Focus");

        List<String> ffmpegCmd = new ArrayList<>();
        ffmpegCmd.add("C:\\ffmpeg\\ffmpeg.exe");
        ffmpegCmd.add("-i");
        ffmpegCmd.add(inputVideoPath.toString());
        ffmpegCmd.add("-y");

        StringBuilder filterChain = new StringBuilder();

        // Apply aggressive crop for focus
        filterChain.append("crop=iw*0.85:ih*0.85:(iw-iw*0.85)/2:(ih-ih*0.85)/2");

        // Add subtle edge enhancement
        filterChain.append(",eq=brightness=0.02:contrast=1.15:saturation=1.1");

        if (job.isApplyMirror()) {
            filterChain.append(",vflip");
        }

        ffmpegCmd.add("-vf");
        ffmpegCmd.add(filterChain.toString());

        // Apply higher speed for V3
        double v3Speed = job.getSpeedMultiplier() > 0 ? job.getSpeedMultiplier() * 1.1 : 1.1;
        ffmpegCmd.add("-filter:a");
        ffmpegCmd.add("atempo=" + v3Speed);

        ffmpegCmd.add(outputPath.toString());

        executeFFmpegCommand(ffmpegCmd);

        if (job.isAddWatermark() || job.isAddSubtitle()) {
            Path tempPath = Paths.get(OUTPUT_DIR, job.getJobId() + "_v3_temp.mp4");
            Files.move(outputPath, tempPath);
            addTextOverlays(job, tempPath, outputPath);
        }

        LOG.info("✓ Variation 3 completed: " + outputPath);
        return outputPath.toString();
    }

    /**
     * Add text overlays (watermark, subtitle) to a video
     */
    private void addTextOverlays(VideoReupJob job, Path inputPath, Path outputPath) throws Exception {
        List<String> cmd = new ArrayList<>();
        cmd.add("C:\\ffmpeg\\ffmpeg.exe");
        cmd.add("-i");
        cmd.add(inputPath.toString());
        cmd.add("-y");

        StringBuilder filterChain = new StringBuilder();

        // Add watermark if enabled
        if (job.isAddWatermark() && job.getWatermarkText() != null && !job.getWatermarkText().isEmpty()) {
            String watermark = escapeText(job.getWatermarkText());
            filterChain.append("drawtext=fontfile='C:/Windows/Fonts/arial.ttf':text='")
                      .append(watermark)
                      .append("':fontsize=24:fontcolor=FFFFFF:x=10:y=10");
        }

        // Add subtitle if enabled
        if (job.isAddSubtitle() && job.getSubtitleText() != null && !job.getSubtitleText().isEmpty()) {
            if (filterChain.length() > 0) filterChain.append(",");
            String subtitle = escapeText(job.getSubtitleText());
            filterChain.append("drawtext=fontfile='C:/Windows/Fonts/arial.ttf':text='")
                      .append(subtitle)
                      .append("':fontsize=28:fontcolor=FFFF00:x=(w-text_w)/2:y=h-text_h-20");
        }

        if (filterChain.length() > 0) {
            cmd.add("-vf");
            cmd.add(filterChain.toString());
        }

        cmd.add(outputPath.toString());

        executeFFmpegCommand(cmd);
    }

    /**
     * Execute FFmpeg command
     */
    private void executeFFmpegCommand(List<String> command) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.contains("frame=") || line.contains("time=")) {
                LOG.finer(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg command failed with exit code: " + exitCode);
        }
    }

    /**
     * Escape special characters for FFmpeg
     */
    private String escapeText(String text) {
        if (text == null) return "";
        return text.replace("'", "\\'")
                   .replace("\\", "\\\\")
                   .replace(":", "\\:");
    }
}

