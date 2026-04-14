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
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Single Video Re-up Service - Ultimate Affiliate Weapon Flow
 * Tối ưu hóa lách bản quyền đa tầng (Speed, Color, Audio, Pixel)
 */
@Service
public class SingleVideoReupService {

    private static final Logger LOG = Logger.getLogger(SingleVideoReupService.class.getName());
    private static final String FFMPEG_PATH = "C:\\ffmpeg\\ffmpeg.exe";
    private static final String OUTPUT_DIR = "src/main/resources/static/videos/reup/";
    private static final Random RNG = new Random();

    /**
     * Process video re-up with advanced anti-detection techniques
     */
    public VideoReupJob processVideoReup(VideoReupJob job, Path inputPath,
                                   boolean applyCrop, boolean enableSpeedRamping,
                                   boolean applyMirror, boolean muteOriginalAudio,
                                   String subtitle) throws Exception {

        Files.createDirectories(Paths.get(OUTPUT_DIR));

        LOG.info("🚀 [AffiliateWeapon] Kích hoạt luồng lách nâng cao cho Job: " + job.getJobId());
        String fileName = "reup_" + job.getJobId() + "_" + System.currentTimeMillis() + ".mp4";
        Path outputPath = Paths.get(OUTPUT_DIR, fileName);

        try {
            // Build Ultimate FFmpeg Command
            List<String> cmd = buildUltimateCommand(inputPath, outputPath, applyCrop, enableSpeedRamping, applyMirror, muteOriginalAudio, subtitle);

            job.setProgressPercent(20);
            executeFFmpegCommand(cmd);

            // Cập nhật kết quả vào job
            List<String> results = new ArrayList<>();
            results.add("/videos/reup/" + fileName);
            job.setOutputVideoPaths(results);
            job.setProgressPercent(100);

            LOG.info("✅ [AffiliateWeapon] Video Re-up hoàn tất thành công");
            return job;

        } catch (Exception e) {
            LOG.severe("❌ Lỗi xử lý vũ khí Re-up: " + e.getMessage());
            throw e;
        }
    }

    private List<String> buildUltimateCommand(Path inputPath, Path outputPath,
                                              boolean applyCrop, boolean enableSpeedRamping,
                                              boolean applyMirror, boolean muteOriginalAudio,
                                              String subtitle) {
        List<String> cmd = new ArrayList<>();
        cmd.add(FFMPEG_PATH);
        cmd.add("-i"); cmd.add(inputPath.toString());
        cmd.add("-y");

        StringBuilder filterChain = new StringBuilder();
        
        // 1. TRIM (Lách thuật toán bằng cách bỏ 0.5s đầu)
        filterChain.append("trim=start=0.5,setpts=PTS-STARTPTS,");

        // 2. SPEED RAMPING (Lách nhịp điệu quét chuyển động)
        if (enableSpeedRamping) {
            // Random tốc độ trong khoảng 0.95x - 1.05x (0.95 <= s <= 1.05)
            double speedFactor = 0.95 + (RNG.nextDouble() * 0.1);
            filterChain.append(String.format(Locale.US, "setpts=%.4f*PTS,", speedFactor));
        }

        // 3. CROP & MIRROR
        if (applyCrop) {
            filterChain.append("scale=iw*1.1:ih*1.1,crop=trunc(iw/1.1/2)*2:trunc(ih/1.1/2)*2,");
        }
        if (applyMirror) {
            filterChain.append("hflip,");
        }

        // 4. COLOR GRADING (Lách thuật toán màu sắc)
        // Adjust: Contrast 1.05, Brightness 0.05, Saturation 1.1 (Tăng nhẹ độ rực rỡ)
        filterChain.append("eq=contrast=1.05:brightness=0.03:saturation=1.1,");

        // 5. PIXEL NOISE (Lách AI quét điểm ảnh)
        // Chèn lớp nhiễu cực nhẹ để thay đổi mã hash của file
        filterChain.append("rgbnoise=c0=0.02:c1=0.02:c2=0.02,");

        // 6. OVERLAY SUBTITLE
        if (subtitle != null && !subtitle.trim().isEmpty()) {
            filterChain.append("drawtext=fontfile='C\\:/Windows/Fonts/arial.ttf':text='")
                      .append(escapeText(subtitle))
                      .append("':fontsize=28:fontcolor=white:x=(w-text_w)/2:y=h-200:box=1:boxcolor=black@0.4:boxborderw=10,");
        }

        String finalFilterChain = filterChain.toString();
        if (finalFilterChain.endsWith(",")) finalFilterChain = finalFilterChain.substring(0, finalFilterChain.length() - 1);

        if (!finalFilterChain.isEmpty()) {
            cmd.add("-vf"); cmd.add(finalFilterChain);
        }

        // 7. AUDIO PROCESSING (Lách thuật toán âm thanh)
        if (muteOriginalAudio) {
            cmd.add("-an"); // Xóa hoàn toàn âm thanh gốc
        } else {
            // Giảm âm lượng xuống 70% để lách quét sóng âm 100%
            cmd.add("-af"); cmd.add("volume=0.7");
            cmd.add("-c:a"); cmd.add("aac"); cmd.add("-b:a"); cmd.add("192k");
        }

        // 8. CODEC & METADATA
        cmd.add("-c:v"); cmd.add("libx264");
        cmd.add("-preset"); cmd.add("veryfast");
        cmd.add("-crf"); cmd.add("18");
        cmd.add("-pix_fmt"); cmd.add("yuv420p");
        
        // Fix FPS 29.97 (Tiêu chuẩn TikTok)
        cmd.add("-r"); cmd.add("29.97");
        
        // Xóa hoàn toàn Metadata để lách nguồn gốc file
        cmd.add("-map_metadata"); cmd.add("-1");
        
        cmd.add("-movflags"); cmd.add("+faststart");
        cmd.add(outputPath.toAbsolutePath().toString());

        return cmd;
    }

    private void executeFFmpegCommand(List<String> command) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while (reader.readLine() != null) {} // Đọc output để tránh treo tiến trình
        int exitCode = process.waitFor();
        if (exitCode != 0) throw new RuntimeException("FFmpeg lách bản quyền thất bại: " + exitCode);
    }

    private String escapeText(String text) {
        return text.replace("'", "\\'").replace(":", "\\:").replace(",", "\\,");
    }
}
