package com.ninhquachhai.tiktoktool.service;

import com.ninhquachhai.tiktoktool.model.VideoScenario;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class VideoRenderService {

    private static final Logger LOG         = Logger.getLogger(VideoRenderService.class.getName());
    
    // Tự động tìm FFmpeg trong thư mục bin của dự án hoặc ổ C
    private String getFFmpegPath() {
        Path localPath = Paths.get("bin/ffmpeg.exe");
        if (Files.exists(localPath)) {
            return localPath.toAbsolutePath().toString();
        }
        return "C:\\ffmpeg\\ffmpeg.exe"; 
    }

    private String getFFprobePath() {
        Path localPath = Paths.get("bin/ffprobe.exe");
        if (Files.exists(localPath)) {
            return localPath.toAbsolutePath().toString();
        }
        return "C:\\ffmpeg\\ffprobe.exe";
    }

    private static final String OUTPUT_DIR  = "src/main/resources/static/videos/";
    private static final String MUSIC_DIR   = "src/main/resources/static/music/";
    private static final String FONT_PATH   = "C\\:/Windows/Fonts/arial.ttf";

    private static final Random RNG = new Random();

    public record ReupOptions(
        boolean isMirror,
        boolean rotate1Degree,
        boolean isCrop,
        boolean addNoiseLayer,
        boolean addOverlayLayer,
        boolean addVignette,
        boolean applyColorFilter,
        String  brightnessMode, // "bright", "dark", "normal"
        boolean enableTrimming,
        String  speedMode,      // "up", "down", "normal"
        boolean muteOriginalAudio,
        boolean mixBackgroundMusic,
        String  subtitle,
        String  videoQuality    // "720p", "1080p", "original"
    ) {
        public static ReupOptions defaults() {
            return new ReupOptions(true, true, true, true, true, true, true, "normal", true, "up", false, true, "", "original");
        }
    }

    public String renderUltimateAffiliate(Path src, ReupOptions opts) throws Exception {
        double duration = getVideoDuration(src);
        
        double startTrim = 0;
        double endTrim = duration;
        if (opts.enableTrimming() && duration > 5.0) {
            startTrim = 2.0;
            endTrim = duration - 2.0;
        }

        String music = pickRandomMusic();
        String fileName = "reup_" + UUID.randomUUID() + ".mp4";
        Path out = Paths.get(OUTPUT_DIR + fileName);
        Files.createDirectories(out.getParent());

        List<String> cmd = new ArrayList<>();
        cmd.add(getFFmpegPath());
        cmd.add("-i"); cmd.add(src.toAbsolutePath().toString());
        if (music != null) {
            cmd.add("-i"); cmd.add(music);
        }

        StringBuilder fc = new StringBuilder();

        // 1. VISUAL FILTERS
        StringBuilder vFilters = new StringBuilder();
        
        vFilters.append(String.format(Locale.US, "trim=start=%.2f:end=%.2f,setpts=PTS-STARTPTS,", startTrim, endTrim));
        
        if (opts.isMirror()) vFilters.append("hflip,");
        
        if (opts.rotate1Degree()) {
            vFilters.append("rotate=1.5*PI/180:fillcolor=black,");
        }
        
        // --- XỬ LÝ CHẤT LƯỢNG VIDEO (Scaling với Lanczos) ---
        if ("720p".equalsIgnoreCase(opts.videoQuality())) {
            vFilters.append("scale=-2:720:flags=lanczos,");
        } else if ("1080p".equalsIgnoreCase(opts.videoQuality())) {
            vFilters.append("scale=-2:1080:flags=lanczos,");
        }

        if (opts.isCrop()) {
            vFilters.append("scale=1.1*iw:-1:flags=lanczos,crop=iw/1.1:ih/1.1,");
        }

        // --- 2. KỸ THUẬT CHỈNH MÀU NÂNG CAO ---
        double contrast = 1.0;
        double brightness = 0.0;
        double saturation = 1.15;
        double gamma = 1.0;

        if (opts.applyColorFilter()) {
            contrast = 1.05; 
            brightness = -0.01;
            saturation = 1.2;
            gamma = 0.98;
        }

        if ("bright".equalsIgnoreCase(opts.brightnessMode())) {
            brightness += 0.04; contrast += 0.02; gamma += 0.1;
        } else if ("dark".equalsIgnoreCase(opts.brightnessMode())) {
            brightness -= 0.08; contrast += 0.05; gamma -= 0.1;
        }

        vFilters.append(String.format(Locale.US, "eq=contrast=%.2f:brightness=%.2f:saturation=%.2f:gamma=%.2f,", 
                       contrast, brightness, saturation, gamma));

        if (opts.addNoiseLayer()) vFilters.append("noise=alls=2:allf=t+u,");
        if (opts.addVignette()) vFilters.append("vignette=PI/4,");
        if (opts.addOverlayLayer()) vFilters.append("drawbox=x=0:y=0:w=iw:h=ih:color=black@0.01:t=fill,");

        // --- TỐC ĐỘ (Speed Mode) ---
        double speedMult = 1.0;
        if ("up".equalsIgnoreCase(opts.speedMode())) speedMult = 1.025;
        else if ("down".equalsIgnoreCase(opts.speedMode())) speedMult = 0.9;
        
        vFilters.append(String.format(Locale.US, "setpts=%.4f*PTS,", 1.0/speedMult));

        if (opts.subtitle() != null && !opts.subtitle().isBlank()) {
            vFilters.append(fmt("drawtext=fontfile='%s':text='%s':fontsize=32:fontcolor=white:" +
                               "box=1:boxcolor=black@0.4:x=(w-text_w)/2:y=h-200,",
                               FONT_PATH, escapeDrawtext(opts.subtitle())));
        }

        String finalV = vFilters.toString();
        if (finalV.endsWith(",")) finalV = finalV.substring(0, finalV.length() - 1);
        fc.append("[0:v]").append(finalV).append("[vout];");

        // 4. AUDIO ENGINEERING
        String aTrim = String.format(Locale.US, "atrim=start=%.2f:end=%.2f,asetpts=PTS-STARTPTS,", startTrim, endTrim);
        String tempoFilter = String.format(Locale.US, "atempo=%.3f", speedMult);

        if (opts.muteOriginalAudio()) {
            if (music != null) {
                fc.append("[1:a]volume=1.0,").append(tempoFilter).append("[aout]"); 
            }
        } else {
            if (music != null) {
                fc.append("[0:a]").append(aTrim).append(tempoFilter).append(",volume=1.0[abase];");
                fc.append("[1:a]volume=0.07[bgm];"); 
                fc.append("[abase][bgm]amix=inputs=2:duration=first:normalize=0[aout]");
            } else {
                fc.append("[0:a]").append(aTrim).append(tempoFilter).append(",volume=1.0[aout]");
            }
        }

        cmd.add("-filter_complex"); cmd.add(fc.toString());
        cmd.add("-map"); cmd.add("[vout]");

        if (opts.muteOriginalAudio() && music == null) {
            cmd.add("-an");
        } else {
            cmd.add("-map"); cmd.add("[aout]");
            cmd.add("-c:a"); cmd.add("aac");
            cmd.add("-b:a"); cmd.add("192k");
        }

        // 6. TỐI ƯU HÓA ENCODING
        cmd.add("-c:v");     cmd.add("libx264");
        cmd.add("-preset");  cmd.add("medium"); 
        
        if ("720p".equalsIgnoreCase(opts.videoQuality())) {
            cmd.add("-crf"); cmd.add("18");
            cmd.add("-maxrate"); cmd.add("5000k");
            cmd.add("-bufsize"); cmd.add("10000k");
        } else if ("1080p".equalsIgnoreCase(opts.videoQuality())) {
            cmd.add("-crf"); cmd.add("17");
            cmd.add("-maxrate"); cmd.add("12000k");
            cmd.add("-bufsize"); cmd.add("24000k");
        } else {
            cmd.add("-crf"); cmd.add("17");
        }

        cmd.add("-pix_fmt"); cmd.add("yuv420p");
        cmd.add("-r");       cmd.add("30"); 
        cmd.add("-map_metadata"); cmd.add("-1");
        cmd.add("-movflags"); cmd.add("+faststart");
        cmd.add("-y");        cmd.add(out.toAbsolutePath().toString());

        return execSimple(cmd, "ToolReup");
    }

    private String execSimple(List<String> cmd, String label) throws Exception {
        LOG.info("[" + label + "] FFmpeg: " + String.join(" ", cmd));
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        String log = new String(process.getInputStream().readAllBytes());
        if (process.waitFor() != 0) {
            LOG.severe("[" + label + "] lỗi:\n" + log);
            throw new RuntimeException("FFmpeg error");
        }
        String lastArg = cmd.get(cmd.size() - 1);
        return "/videos/" + Paths.get(lastArg).getFileName().toString();
    }

    private double getVideoDuration(Path videoPath) throws Exception {
        String ffprobePath = getFFprobePath();
        ProcessBuilder pb = new ProcessBuilder(
            ffprobePath, "-v", "error", "-show_entries", "format=duration",
            "-of", "default=noprint_wrappers=1:nokey=1", videoPath.toAbsolutePath().toString()
        );
        Process process = pb.start();
        String output = new String(process.getInputStream().readAllBytes()).trim();
        process.waitFor();
        return Double.parseDouble(output);
    }

    private String pickRandomMusic() {
        try {
            List<Path> files = Files.list(Paths.get(MUSIC_DIR))
                .filter(p -> p.toString().toLowerCase().matches(".*\\.(mp3|wav|m4a)$"))
                .toList();
            if (files.isEmpty()) return null;
            return files.get(RNG.nextInt(files.size())).toAbsolutePath().toString();
        } catch (Exception e) { return null; }
    }

    private String escapeDrawtext(String text) {
        return text.replace("\\", "\\\\").replace("'", "\\'").replace(":", "\\:").replace(",", "\\,");
    }

    private String fmt(String pattern, Object... args) {
        return String.format(Locale.US, pattern, args);
    }

    public String processReupWithOptions(Path videoPath, int videoIndex, VideoScenario scenario, ReupOptions opts) throws Exception {
        return renderUltimateAffiliate(videoPath, opts);
    }
}
