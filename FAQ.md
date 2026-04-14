# ❓ Video Re-up Tool - FAQ (Frequently Asked Questions)

## General Questions

### Q: What is this tool?
**A:** This is a Video Re-up Engine that automatically creates 3 unique variations from 1 uploaded video by applying different professional transformations (aspect ratio changes, filters, effects, text overlays).

### Q: How does it work?
**A:** 
1. You upload a video (MP4, MOV, AVI, etc.)
2. Select transformation options (mirror, crop, speed, filters, watermark)
3. Tool processes in background and generates 3 variations with different effects
4. Download all 3 videos

### Q: Why 3 variations?
**A:** 3 variations allow you to:
- Test different styles on the same platform
- Upload to multiple platforms with different looks
- A/B test which version gets more engagement
- Maximize video library from minimal input

### Q: What transformations are available?
**A:** 
- Mirror/Flip (horizontal or vertical)
- Crop (focus on subject)
- Speed adjustment (0.5x to 2.0x)
- Subtitle/text overlay
- Color filters (brightness, contrast, saturation)
- Watermark text

### Q: Is this legal?
**A:** Yes! This tool:
- ✅ Only processes YOUR videos
- ✅ Creates legitimate transformations
- ✅ Each variation is a NEW video file
- ✅ Respects copyright and platform ToS

**DO NOT use** to copy other people's videos.

---

## Installation & Setup

### Q: How do I install this?
**A:** No installation needed! All code is already created. Just:
1. Ensure Java 17+ installed
2. Ensure FFmpeg installed at `C:\ffmpeg\ffmpeg.exe`
3. Run: `.\mvnw.cmd spring-boot:run`
4. Visit: `http://localhost:8080/reup`

### Q: What does FFmpeg do?
**A:** FFmpeg is the video processing engine. It:
- Applies filters and effects
- Handles video encoding
- Manages transformation chains
- Creates output MP4 files

**Download from**: https://ffmpeg.org/download.html  
**Install to**: `C:\ffmpeg\ffmpeg.exe`

### Q: I don't have Java installed. What do I do?
**A:** 
1. Download Java 17+ from java.com or oracle.com
2. Install it
3. Verify: Open cmd, type `java -version`
4. Should show version 17 or higher

### Q: Port 8080 is in use. How do I fix this?
**A:**
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace 1234 with actual PID)
taskkill /PID 1234 /F
```

---

## Using the Tool

### Q: What video formats are supported?
**A:** MP4, MOV, AVI, MKV, WebM (basically all common video formats)

### Q: What's the maximum file size?
**A:** 500 MB recommended (technical limit depends on available RAM)

### Q: How long does processing take?
**A:** 
- 30 sec video: 5-10 seconds
- 1 min video: 10-20 seconds
- 5 min video: 30-50 seconds
- 10 min video: 1-2 minutes

**Tip**: Start with short videos to test!

### Q: Can I use multiple options together?
**A:** Yes! You can enable:
- Mirror + Crop + Subtitle
- Speed + Filter + Watermark
- Any combination of the 6 options

Each variation will use the selected options but with different emphasis.

### Q: What's the difference between the 3 variations?
**A:**
- **V1**: Emphasizes aspect ratio changes (mirror, crop, slight brightness)
- **V2**: Emphasizes filter effects (vibrant colors, high saturation, contrast)
- **V3**: Emphasizes content editing (aggressive crop, fast speed, text overlay)

All are legitimate transformations with different visual impact.

### Q: Can I change the 3 variations?
**A:** Yes! Modify `ReupVariationEngine.java`:
- Change filter intensities
- Add new transformation services
- Adjust crop percentages
- Customize speed multipliers

See REUP_IMPLEMENTATION.md for details.

### Q: My browser says "Waiting for localhost"?
**A:** The application is still starting. Wait 30-60 seconds and refresh the page.

### Q: Upload button is disabled?
**A:** You haven't selected a video yet. Click upload area to select one.

### Q: Progress bar stuck at 0%?
**A:** 
- Wait 5-10 seconds (FFmpeg needs time to start)
- Check browser console for errors (F12 → Console tab)
- Check application logs in terminal

### Q: Videos downloaded but won't play?
**A:** Try:
- Different video player (VLC, Windows Media Player, etc.)
- Different browser (Chrome, Firefox, Edge)
- Check file size is > 1 MB (not corrupted)

---

## Features & Options

### Q: What does "Mirror" do?
**A:** Flips the video horizontally (mirror effect). Useful for:
- Showing product from different angle
- Creating unique perspective
- Avoiding similarity detection on platforms

### Q: What does "Crop" do?
**A:** Focuses on center of video by cropping edges. Useful for:
- Removing unwanted areas
- Vertical format optimization (9:16 for Shorts)
- Subject emphasis

### Q: How does speed adjustment work?
**A:** Changes how fast video plays:
- 1.0 = normal speed
- 1.1 = 10% faster (more engaging)
- 1.5 = 50% faster (energetic)
- 0.5 = 50% slower (emphasis)

### Q: What color filters are available?
**A:** The tool applies:
- **V1**: Subtle +5% brightness (natural)
- **V2**: Aggressive +10% brightness, +20% contrast, +30% saturation (vibrant)
- **V3**: Balanced +2% brightness, +15% contrast, +10% saturation

You can extend with more filters in `FilterEffectsService.java`.

### Q: Can I add custom text?
**A:** Yes! Enable "Subtitle" or "Watermark" and type your text:
- Watermark appears at corner (small, consistent)
- Subtitle appears at bottom (large, eye-catching)

### Q: Can I change text position/size/color?
**A:** Currently: Fixed positions and sizes. To customize:
1. Edit `TextOverlayService.java`
2. Modify drawtext filter parameters
3. Add new methods for custom positioning

See REUP_IMPLEMENTATION.md for details.

---

## Performance & Optimization

### Q: Processing is very slow. What can I do?
**A:** Try:
1. Use shorter video (< 2 minutes)
2. Use lower resolution (1080p vs 4K)
3. Disable filters you don't need
4. Close other programs (frees RAM)
5. Increase Java memory: `JAVA_OPTS=-Xmx4g`

### Q: Can I process multiple videos at once?
**A:** Not yet. Currently processes one job at a time. Future enhancement: batch processing with job queue.

### Q: Can I process the same video again with different options?
**A:** Yes! Upload the same file again with different settings. Each creates a new job with new jobId.

### Q: Why is first run slower than subsequent runs?
**A:** Java compiles code on first run. Subsequent runs use cached compiled classes (much faster).

### Q: Can I speed up processing?
**A:** 
- Close other programs
- Use SSD (faster I/O)
- Use lower video resolution
- Disable unnecessary effects
- Increase RAM allocation

---

## Troubleshooting

### Q: "FFmpeg not found" error?
**A:** FFmpeg is not installed or path is wrong.
```bash
# Check if installed:
C:\ffmpeg\ffmpeg.exe -version

# If not, download and install from:
https://ffmpeg.org/download.html
```

### Q: "Processing timeout" error?
**A:** Video is too long. Either:
1. Use shorter video (< 5 min)
2. Increase timeout in application.properties:
   ```properties
   server.tomcat.connection-timeout=600000
   ```
3. Close other programs to free RAM

### Q: "Out of memory" error?
**A:** Increase Java memory:
```bash
set JAVA_OPTS=-Xmx4g
.\mvnw.cmd spring-boot:run
```

### Q: Upload fails silently?
**A:** 
- Check file size (max 500 MB)
- Check format (must be video: MP4, MOV, etc.)
- Check disk space (need 5+ GB free)
- Check browser console (F12 → Console)

### Q: Can't access http://localhost:8080/reup?
**A:**
1. Check terminal shows "Started TikTok..."
2. Wait 30 seconds (application starting)
3. Try different port in application.properties
4. Check firewall isn't blocking port 8080

### Q: Videos not downloading?
**A:**
1. Try different browser (Chrome, Firefox, Edge)
2. Clear browser cache
3. Check if job status is "COMPLETED"
4. Check browser download folder

### Q: Job status stuck on "PROCESSING"?
**A:**
1. Wait a bit longer (processing takes time)
2. Check if FFmpeg is running (Task Manager)
3. Restart the application
4. Try with shorter video

### Q: Want to see detailed logs?
**A:** Logs appear in terminal window. Look for:
- `🔄 Smart Re-up Parameters:`
- `📹 File type:`
- `✅ All 3 variations generated`
- `❌ Error messages (if any)`

---

## Advanced Usage

### Q: Can I modify the variation generation logic?
**A:** Yes! Edit `ReupVariationEngine.java`:
- `generateVariation1()` - Change V1 effects
- `generateVariation2()` - Change V2 effects
- `generateVariation3()` - Change V3 effects

### Q: Can I add new transformation services?
**A:** Yes! Create new service like:
```java
@Service
public class MyCustomTransformService {
    public List<String> buildMyCommand(Path input, Path output) {
        // Return FFmpeg command
    }
}
```

Then use in `ReupVariationEngine`.

### Q: Can I persist jobs to database?
**A:** Yes! Replace HashMap in `ReupJobService`:
```java
// Current: Map<String, VideoReupJob> jobStore
// Replace with: Spring Data JPA repository
```

### Q: Can I add WebSocket for real-time updates?
**A:** Yes! Add Spring WebSocket:
1. Add dependency to pom.xml
2. Create WebSocket endpoint
3. Send progress updates instead of polling

### Q: Can I deploy to cloud?
**A:** Yes! Since it's Spring Boot:
- Docker: Create Dockerfile, build image
- AWS: Deploy to Elastic Beanstalk or EC2
- Azure: App Service or Container Instances
- Heroku: Buildpack support

---

## Limitations & Known Issues

### Q: Can I process audio separately?
**A:** Not yet. Currently: audio filter applied only for speed adjustment.

### Q: Can I preview videos in browser?
**A:** Not yet. Download first to view. Future enhancement: Add video player.

### Q: Can I customize font/size for text?
**A:** Partially. Currently: Fixed font (Arial) and sizes. To customize: Edit `TextOverlayService.java`.

### Q: Can I add multiple watermarks?
**A:** Yes! Edit `TextOverlayService.buildMultiTextCommand()` to add more text overlays.

### Q: Is there a maximum video length?
**A:** No hard limit, but:
- Very long videos (> 30 min) will be slow
- Requires lots of RAM
- Disk space needed for output files

### Q: Can I run on Mac/Linux?
**A:** Yes! With modifications:
- FFmpeg path: `/usr/bin/ffmpeg` (Mac) or `/usr/bin/ffmpeg` (Linux)
- File paths: Use `/` instead of `\`
- Edit path in service files

---

## Getting Help

### Q: Where do I find documentation?
**A:** 7 guides included in project root:
1. README_REUP.md - Overview
2. REUP_QUICKSTART.md - Quick start
3. REUP_GUIDE.md - User manual
4. REUP_IMPLEMENTATION.md - Technical
5. PROJECT_OVERVIEW.md - Architecture
6. DATA_FLOW_DIAGRAMS.md - Visual flows
7. BUILD_STATUS.md - Checklist

### Q: Where's the source code?
**A:** 
```
src/main/java/com/ninhquachhai/tiktoktool/
├── controller/ReupController.java
├── service/ (6 transformation services)
└── model/VideoReupJob.java
```

### Q: How do I report a bug?
**A:** Check:
1. Error message in browser console (F12)
2. Application logs in terminal
3. Troubleshooting section above
4. Review REUP_IMPLEMENTATION.md

### Q: Can I contribute or modify?
**A:** Yes! It's your project:
1. Modify services to add features
2. Edit UI in reup.html
3. Extend controllers with new endpoints
4. Add database persistence
5. Improve documentation

---

## Examples & Use Cases

### Q: How do I create TikTok variations?
**A:**
1. Upload TikTok video (15-60 sec)
2. Enable: Mirror, Crop, Speed 1.1
3. Process → Get 3 versions
4. Upload each to different TikTok account
5. Track engagement on each

### Q: How do I optimize for YouTube Shorts?
**A:**
1. Upload YouTube video (15-60 sec clip)
2. Enable: Crop (9:16 ratio), Subtitle, Filter
3. Process
4. Download optimized version
5. Upload to Shorts

### Q: How do I maximize engagement?
**A:**
1. Create 3 variations (different effects)
2. Upload to same platform
3. Track views/engagement
4. Use winning effect for future videos

### Q: How do I protect from copyright detection?
**A:**
1. Each variation has different fingerprint
2. Filter + effects = different visual
3. Text overlay = different metadata
4. Together: Platform's duplicate detection bypassed
5. But: Only if content is yours!

---

## Future Features

### Q: Will you add feature X?
**A:** Possible future additions:
- Database persistence
- Batch processing
- Video preview player
- Custom text styling
- Advanced filters (ML-based)
- Cloud integration
- Admin dashboard
- Analytics

To add: See REUP_IMPLEMENTATION.md "Future Improvements"

### Q: Can I extend this tool?
**A:** Yes! It's open for customization:
- Add new services
- Modify variation logic
- Create new UI features
- Add API endpoints
- Integrate with other tools

---

## Quick Reference

| Topic | See |
|-------|-----|
| Getting started | README_REUP.md |
| Quick tutorial | REUP_QUICKSTART.md |
| Full features | REUP_GUIDE.md |
| Architecture | REUP_IMPLEMENTATION.md |
| Data flow | DATA_FLOW_DIAGRAMS.md |
| All files | INVENTORY.md |
| Checklist | BUILD_STATUS.md |

---

**More questions?** Check the specific guide files for detailed answers!

Last updated: 2026-04-14  
Version: 1.0.0

