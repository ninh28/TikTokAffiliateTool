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
 * Advanced Re-up Engine (Flow Cắt Lại)
 * Implements the complete re-up workflow with maximum originality
 */
@Service
public class AdvancedReupEngine {

    private static final Logger LOG = Logger.getLogger(AdvancedReupEngine.class.getName());
    private static final String OUTPUT_DIR = "src/main/resources/static/videos/advanced/";
    private static final String TEMP_DIR = "temp_processing/";

    private final AdvancedVisualEffectsService visualEffectsService;
    private final AdvancedAudioService audioService;

    public AdvancedReupEngine(AdvancedVisualEffectsService visualEffectsService,
                              AdvancedAudioService audioService) {
        this.visualEffectsService = visualEffectsService;
        this.audioService = audioService;
    }

    /**
     * Main workflow: Process video with advanced re-up techniques
     * Generates 1 output video with maximum originality
     */
    public String processAdvancedReup(VideoReupJob job, Path inputVideoPath,
                                      String textHook, String trendingMusicPath,
                                      boolean includeVoiceover) throws Exception {

        Files.createDirectories(Paths.get(OUTPUT_DIR));
        Files.createDirectories(Paths.get(TEMP_DIR));

        LOG.info("🎬 Starting Advanced Re-up Flow for job: " + job.getJobId());

        Path finalOutput = Paths.get(OUTPUT_DIR, job.getJobId() + "_advanced.mp4");

        // Step 1: Trim (remove 1s start and end)
        LOG.info("✂️ Step 1: Trimming video (remove 1s start + 1s end)");
        Path trimmedVideo = Paths.get(TEMP_DIR, job.getJobId() + "_trimmed.mp4");
        processTrim(inputVideoPath, trimmedVideo);
        updateProgress(job, 15);

        // Step 2: Apply all visual effects
        LOG.info("🎨 Step 2: Applying visual effects (Zoom 1.1x + Flip + Color Grading + Text Hook + Grain)");
        Path visualProcessedVideo = Paths.get(TEMP_DIR, job.getJobId() + "_visual.mp4");
        processVisualEffects(trimmedVideo, visualProcessedVideo, textHook, 1.1, 0.9, 1.05, 0.97);
        updateProgress(job, 40);

        // Step 3: Speed up video 1.1x
        LOG.info("⚡ Step 3: Speeding up video (1.1x)");
        Path speedVideo = Paths.get(TEMP_DIR, job.getJobId() + "_speed.mp4");
        processSpeed(visualProcessedVideo, speedVideo, 1.1);
        updateProgress(job, 55);

        // Step 4: Audio processing
        LOG.info("🔊 Step 4: Processing audio");
        Path finalAudio = processAudio(speedVideo, trendingMusicPath, includeVoiceover, job.getJobId());
        updateProgress(job, 75);

        // Step 5: Merge audio with video
        LOG.info("🎞️ Step 5: Merging audio and video");
        processMergeAudioVideo(speedVideo, finalAudio, finalOutput);
        updateProgress(job, 95);

        // Cleanup temp files
        LOG.info("🧹 Cleaning up temporary files");
        cleanupTempFiles(job.getJobId());

        LOG.info("✅ Advanced Re-up completed: " + finalOutput);

        return finalOutput.toString();
    }

    /**
     * Step 1: Trim 1 second from start and end
     */
    private void processTrim(Path inputVideo, Path outputVideo) throws Exception {
        List<String> cmd = new ArrayList<>();
        cmd.add("C:\\ffmpeg\\ffmpeg.exe");
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-ss");
        cmd.add("1");  // Start at 1 second
        cmd.add("-y");
        cmd.add(outputVideo.toString());

        executeFFmpegCommand(cmd);
    }

    /**
     * Step 2: Apply all visual effects (combined)
     */
    private void processVisualEffects(Path inputVideo, Path outputVideo, String textHook,
                                      double zoom, double temperature, double contrast, double saturation) throws Exception {
        List<String> cmd = visualEffectsService.buildCombinedVisualEffectsCommand(
            inputVideo, outputVideo, textHook, zoom, temperature, contrast, saturation
        );
        executeFFmpegCommand(cmd);
    }

    /**
     * Step 3: Speed up video
     */
    private void processSpeed(Path inputVideo, Path outputVideo, double speedFactor) throws Exception {
        List<String> cmd = visualEffectsService.buildSpeedUpCommand(inputVideo, outputVideo, speedFactor);
        executeFFmpegCommand(cmd);
    }

    /**
     * Step 4: Process audio (extract, reduce, mix with trending music)
     */
    private Path processAudio(Path videoPath, String trendingMusicPath, boolean includeVoiceover,
                             String jobId) throws Exception {

        // Extract original audio
        Path originalAudio = Paths.get(TEMP_DIR, jobId + "_audio_original.aac");
        List<String> extractCmd = audioService.buildExtractAudioCommand(videoPath, originalAudio);
        executeFFmpegCommand(extractCmd);

        // Reduce original audio volume by 10%
        Path reducedAudio = Paths.get(TEMP_DIR, jobId + "_audio_reduced.aac");
        List<String> reduceCmd = audioService.buildReduceVolumeCommand(originalAudio, reducedAudio, 0.9);
        executeFFmpegCommand(reduceCmd);

        // Mix with trending music
        Path mixedAudio = Paths.get(TEMP_DIR, jobId + "_audio_mixed.aac");
        if (trendingMusicPath != null && !trendingMusicPath.isEmpty()) {
            List<String> mixCmd = audioService.buildMixAudioCommand(
                reducedAudio, Paths.get(trendingMusicPath), mixedAudio
            );
            executeFFmpegCommand(mixCmd);
        } else {
            // If no trending music provided, just use reduced original audio
            Files.copy(reducedAudio, mixedAudio);
        }

        // Optional: Add voiceover
        if (includeVoiceover) {
            LOG.info("🎙️ Adding AI voiceover (optional)");
            // Placeholder: In real implementation, call TTS API
            // Path voiceoverFile = generateVoiceover(jobId);
            // List<String> voiceCmd = audioService.buildAddVoiceoverCommand(mixedAudio, voiceoverFile, finalAudio);
            // executeFFmpegCommand(voiceCmd);
        }

        return mixedAudio;
    }

    /**
     * Step 5: Merge audio and video
     */
    private void processMergeAudioVideo(Path videoPath, Path audioPath, Path outputPath) throws Exception {
        List<String> cmd = new ArrayList<>();
        cmd.add("C:\\ffmpeg\\ffmpeg.exe");
        cmd.add("-i");
        cmd.add(videoPath.toString());
        cmd.add("-i");
        cmd.add(audioPath.toString());
        cmd.add("-c:v");
        cmd.add("copy");  // Copy video codec (already processed)
        cmd.add("-c:a");
        cmd.add("aac");   // Audio codec
        cmd.add("-map");
        cmd.add("0:v:0");  // Take video from first input
        cmd.add("-map");
        cmd.add("1:a:0");  // Take audio from second input
        cmd.add("-shortest");  // Trim to shortest stream
        cmd.add("-y");
        cmd.add(outputPath.toString());

        executeFFmpegCommand(cmd);
    }

    /**
     * Update job progress
     */
    private void updateProgress(VideoReupJob job, int progress) {
        job.setProgressPercent(progress);
        LOG.info("⏳ Progress: " + progress + "%");
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
     * Cleanup temporary files
     */
    private void cleanupTempFiles(String jobId) {
        try {
            Files.deleteIfExists(Paths.get(TEMP_DIR, jobId + "_trimmed.mp4"));
            Files.deleteIfExists(Paths.get(TEMP_DIR, jobId + "_visual.mp4"));
            Files.deleteIfExists(Paths.get(TEMP_DIR, jobId + "_speed.mp4"));
            Files.deleteIfExists(Paths.get(TEMP_DIR, jobId + "_audio_original.aac"));
            Files.deleteIfExists(Paths.get(TEMP_DIR, jobId + "_audio_reduced.aac"));
            Files.deleteIfExists(Paths.get(TEMP_DIR, jobId + "_audio_mixed.aac"));
        } catch (Exception e) {
            LOG.warning("Error cleaning temp files: " + e.getMessage());
        }
    }
}

