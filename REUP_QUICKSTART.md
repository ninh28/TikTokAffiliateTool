# ⚡ Video Re-up Tool - Quick Start Guide

## 🎯 In 5 Minutes

### 1️⃣ Start the Application
```bash
cd C:\Users\admin\IdeaProjects\TikTokAffiliateTool
.\mvnw.cmd spring-boot:run
```

Wait for: `Started TikTokAffiliateToolApplication in X.XXX seconds`

### 2️⃣ Open in Browser
```
http://localhost:8080/reup
```

You should see the Video Re-up Tool interface with a purple gradient theme.

### 3️⃣ Upload Video
- Click the upload area or drag a video file
- Supported: MP4, MOV, AVI, MKV, WebM
- Size: Recommended under 500MB

### 4️⃣ Choose Effects
Toggle the options you want:
- ✅ **Mirror**: Flip video horizontally
- ✅ **Crop**: Focus on center (default enabled)
- 🎚️ **Speed**: Set video speed (default 1.1 = 10% faster)
- 📝 **Subtitle**: Add text to video
- ✨ **Filter**: Apply color effects
- 🏷️ **Watermark**: Add logo/text to corner

### 5️⃣ Process
Click **"Bắt Đầu Xử Lý"** (Start Processing)

Wait for the progress bar to reach 100% ⏳

### 6️⃣ Download
3 video variations appear → Click **"Tải Xuống"** to download each one

---

## 📱 What Gets Created?

### Variation 1 🎯
- **Effect**: Mirror + Crop + Light Brightness
- **Use Case**: Different perspective of same content

### Variation 2 🌈
- **Effect**: Vibrant colors + High saturation
- **Use Case**: Eye-catching, energetic version

### Variation 3 ⚡
- **Effect**: Aggressive crop + Higher speed + Text overlay
- **Use Case**: Fast-paced, action-focused version

---

## 🔧 Common Issues

### ❌ "FFmpeg not found"
**Fix**: Ensure FFmpeg installed at `C:\ffmpeg\ffmpeg.exe`
```bash
# Verify:
C:\ffmpeg\ffmpeg.exe -version
```

### ❌ "Processing timeout"
**Fix**: Video too long. Try a shorter clip or:
- Edit `application.properties`:
  ```properties
  server.tomcat.connection-timeout=600000
  ```

### ❌ Browser won't connect
**Fix**: Check port 8080 is available:
```bash
# Kill process on 8080 if needed:
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### ❌ Upload fails
**Fix**: Check file size and format
- Max: 500MB recommended
- Formats: MP4, MOV, AVI, MKV, WebM

---

## 🎨 Advanced Usage Examples

### Example 1: TikTok Repost
**Goal**: Create 3 versions of same video for different accounts

**Settings**:
```
✅ Mirror: ON (flip perspective)
✅ Crop: ON (focus on subject)
Speed: 1.1 (10% faster = more engaging)
✅ Watermark: ON (add "Link in Bio")
```

**Result**: 3 unique-looking videos from 1 original ✓

### Example 2: YouTube Shorts
**Goal**: Optimize one clip for Shorts

**Settings**:
```
Mirror: OFF (keep original)
✅ Crop: ON (vertical 9:16 aspect)
Speed: 1.15 (snappier)
✅ Subtitle: ON ("Key point here!")
✅ Filter: ON (vibrant colors)
```

**Result**: Professional, short-form version ✓

### Example 3: Facebook Story Series
**Goal**: Create story sequence from one video

**Settings**:
```
✅ Mirror: ON (Variation 1)
✅ Crop: ON (medium)
Speed: 1.0 (normal)
✅ Watermark: ON (brand logo)
```

**Result**: 3 story-ready clips with branding ✓

---

## 📊 Performance Tips

| Video Length | Est. Time | Optimization |
|------------|-----------|--------------|
| < 1 min | 5-10s | ✅ Fast |
| 1-2 min | 10-20s | ✅ Good |
| 2-5 min | 20-50s | ⚠️ Take break |
| 5-10 min | 50-100s | ⚠️ Grab coffee |
| > 10 min | 100s+ | ❌ Split video |

**Tips to speed up:**
- Use lower resolution (1080p vs 4K)
- Keep videos under 5 min
- Disable unused effects
- Close other programs (frees RAM)

---

## 📋 File Organization

### Upload Storage
```
temp_uploads/reup/
  └── [timestamp]_[filename]
```

### Output Videos
```
src/main/resources/static/videos/reup/
  └── [jobId]_v1.mp4
  └── [jobId]_v2.mp4
  └── [jobId]_v3.mp4
```

### Logs
```
Console output shows:
- ⬆️ Upload progress
- 🔄 Processing steps
- ✅ Completion status
```

---

## 🎬 Next Steps

After processing:

1. **Download 3 videos** to your computer
2. **Rename** them with descriptive names:
   - `product_v1_mirror.mp4`
   - `product_v2_vibrant.mp4`
   - `product_v3_fast.mp4`
3. **Upload to platforms**:
   - TikTok, Instagram, YouTube, Facebook, etc.
4. **Track performance**: Which version gets most engagement?
5. **Repeat**: Process more videos with successful settings

---

## ⚙️ System Requirements

✅ **Minimum**:
- Windows 10/11
- 4GB RAM
- Java 17+
- 5GB free disk space

✅ **Recommended**:
- Windows 11
- 8GB+ RAM
- Java 17+
- SSD (faster processing)
- 20GB+ free disk space

✅ **Required**:
- FFmpeg installed (`C:\ffmpeg\ffmpeg.exe`)
- Spring Boot 4.0.5+

---

## 🚨 Important Notes

⚠️ **Legal**: 
- Only process **YOUR OWN** original videos
- Not for copying others' content
- Respect copyright and platform ToS

⚠️ **Quality**: 
- Each variation is a **NEW** video (not just re-encoded)
- Different effects applied = different fingerprint
- Better for avoiding detection on platforms

⚠️ **Performance**:
- First run compiles Java code (may be slow)
- Subsequent runs are faster
- Processing happens **server-side** (takes time)

---

## 💡 Pro Tips

1. **Test with short video first** (15-30 sec)
   - Fast processing = quick feedback loop
   - See if you like the variations

2. **Keep watermark consistent** across uploads
   - Helps build brand recognition
   - Shows video source

3. **Experiment with speed settings**
   - 1.05 = Subtle (10-15% niche audiences prefer slower)
   - 1.1 = Standard (10% faster = more engaging)
   - 1.2+ = Aggressive (energetic, action-heavy)

4. **Use platform-specific subtitles**
   - TikTok: Trendy, short captions
   - YouTube: Informative, longer text
   - Instagram: Motivational, emoji-included

5. **Monitor your results**
   - Track which variation performs best
   - Use those settings for future videos

---

## 📞 Troubleshooting Quick Links

| Problem | Solution |
|---------|----------|
| Port 8080 in use | Kill process: `taskkill /PID [PID] /F` |
| FFmpeg error | Install: [ffmpeg.org](https://ffmpeg.org/download.html) |
| Out of memory | Increase: `JAVA_OPTS=-Xmx4g` |
| Slow processing | Use smaller video or disable filters |
| File won't upload | Check size (<500MB) and format (MP4, MOV, etc.) |
| Download broken | Try different browser or clear cache |

---

## 📚 Full Documentation

- **Detailed Guide**: See `REUP_GUIDE.md`
- **Implementation Details**: See `REUP_IMPLEMENTATION.md`
- **Architecture**: See project structure in IDE

---

**Ready to create viral content? 🚀 Start uploading!**

Last updated: 2024  
Version: 1.0.0 Video Re-up Tool

