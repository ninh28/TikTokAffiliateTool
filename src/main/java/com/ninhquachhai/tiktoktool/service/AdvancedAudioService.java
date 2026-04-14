package com.ninhquachhai.tiktoktool.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Advanced Audio Processing Service
 * Handles audio extraction, mixing, voiceover, and sound design
 */
@Service
public class AdvancedAudioService {

    private static final Logger LOG = Logger.getLogger(AdvancedAudioService.class.getName());
    private static final String FFMPEG_PATH = "C:\\ffmpeg\\ffmpeg.exe";

    /**
     * Extract audio from video
     */
    public List<String> buildExtractAudioCommand(Path inputVideo, Path outputAudio) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputVideo.toString());
        cmd.add("-q:a");
        cmd.add("9");
        cmd.add("-n"); // No overwrite
        cmd.add(outputAudio.toString());
        return cmd;
    }

    /**
     * Reduce audio volume by percentage
     * @param inputAudio Input audio file
     * @param outputAudio Output audio file
     * @param volumeReduction Reduction percentage (0.9 = -10%, 0.8 = -20%)
     */
    public List<String> buildReduceVolumeCommand(Path inputAudio, Path outputAudio, double volumeReduction) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputAudio.toString());
        cmd.add("-af");
        cmd.add("volume=" + volumeReduction);
        cmd.add("-y");
        cmd.add(outputAudio.toString());
        return cmd;
    }

    /**
     * Mix two audio tracks
     * @param audio1 Original audio (reduced volume)
     * @param audio2 Trending music (full volume)
     * @param output Output mixed audio
     */
    public List<String> buildMixAudioCommand(Path audio1, Path audio2, Path output) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(audio1.toString());
        cmd.add("-i");
        cmd.add(audio2.toString());
        cmd.add("-filter_complex");
        cmd.add("[0:a][1:a]amix=inputs=2:duration=first[a]");
        cmd.add("-map");
        cmd.add("[a]");
        cmd.add("-y");
        cmd.add(output.toString());
        return cmd;
    }

    /**
     * Add voiceover to audio track
     * Uses ffmpeg concat demuxer to add voiceover at specific points
     */
    public List<String> buildAddVoiceoverCommand(Path backgroundAudio, Path voiceoverFile, Path output) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(backgroundAudio.toString());
        cmd.add("-i");
        cmd.add(voiceoverFile.toString());
        cmd.add("-filter_complex");
        cmd.add("[0:a][1:a]amix=inputs=2:duration=first[a]");
        cmd.add("-map");
        cmd.add("[a]");
        cmd.add("-y");
        cmd.add(output.toString());
        return cmd;
    }

    /**
     * Normalize audio levels
     */
    public List<String> buildNormalizeAudioCommand(Path inputAudio, Path outputAudio) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputAudio.toString());
        cmd.add("-af");
        cmd.add("loudnorm=I=-16:TP=-1.5:LRA=11");
        cmd.add("-y");
        cmd.add(outputAudio.toString());
        return cmd;
    }

    /**
     * Create fade in effect for audio
     */
    public List<String> buildAudioFadeInCommand(Path inputAudio, Path outputAudio, int durationSeconds) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i");
        cmd.add(inputAudio.toString());
        cmd.add("-af");
        cmd.add("afade=t=in:st=0:d=" + durationSeconds);
        cmd.add("-y");
        cmd.add(outputAudio.toString());
        return cmd;
    }
}

