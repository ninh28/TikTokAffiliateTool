# 🎬 Modern TikTok Re-up Tool - User & Developer Guide

## 📌 What's New

You now have a **completely redesigned Modern Re-up Tool** with:

✅ **Single Video Output** - 1 video upload → 1 re-up video output (no 3 variants)  
✅ **Modern UI** - Clean, minimal, professional interface  
✅ **BEM Class Naming** - Easy to customize CSS  
✅ **Auto Mode** - One-click re-up with all recommended settings  
✅ **Expert Mode** - Fine-grained control over transformations  
✅ **Real-time Progress** - Live status updates  

---

## 🚀 How to Access

### Start the Application
```bash
cd C:\Users\admin\IdeaProjects\TikTokAffiliateTool
.\mvnw.cmd spring-boot:run
```

### Open Modern UI
```
http://localhost:8080/reup/modern
```

---

## 🎨 UI Layout (3 Blocks)

### Block 1: Upload Zone
```
📁 Large drop zone with cloud icon
   └─ "Kéo thả video vào đây"
   └─ Supports: MP4, MOV, AVI, MKV, WebM
   └─ Max size: 500MB
```

### Block 2: Settings
```
⚙️  Settings Section
    ├─ Auto Mode Toggle (Recommended) - Switch for one-click re-up
    └─ Expert Mode (when Auto is OFF)
        ├─ 📐 Crop & Zoom (1.1x)
        ├─ ⚡ Speed 1.1x
        ├─ 🔄 Mirror Flip
        └─ 🔇 Mute Audio
```

### Block 3: Content & Progress
```
📝 Subtitle Input
   └─ Optional text overlay
   └─ Placed at bottom of video

⏳ Progress Bar
   └─ Hidden by default
   └─ Shows 0-100% during processing
```

### Main Action
```
🎯 Full-width Button
   └─ "BẮT ĐẦU RE-UP"
   └─ Purple-to-blue gradient
   └─ Changes to "TẢI XUỐNG VIDEO" on completion
```

---

## 🔄 Processing Flow

### Auto Mode (Default)
```
User uploads video
    ↓
Auto toggles ON (default)
    ↓
All recommended settings applied:
  ✓ Trim 0.5s start + end
  ✓ Crop & Zoom 1.1x
  ✓ Speed 1.1x
  ✓ Color grading (brightness +5%, contrast +10%)
  ✓ Grain overlay 3%
  ✓ Optional subtitle
    ↓
1 re-up video output
    ↓
Download link provided
```

### Expert Mode
```
User uploads video
    ↓
Auto toggles OFF
    ↓
Expert cards appear:
  • Toggle Crop/Zoom individually
  • Toggle Speed individually
  • Toggle Mirror individually
  • Toggle Audio mute individually
    ↓
Custom combination applied
    ↓
1 re-up video output
    ↓
Download link provided
```

---

## 📋 Transformations Applied

### Default (Auto Mode)
```
1. ✂️ TRIM
   Remove 0.5s from start and end

2. 🔍 CROP & ZOOM (1.1x)
   Scale up 1.1x, crop center
   
3. ⚡ SPEED (1.1x)
   Play 10% faster, audio tempo adjusted

4. 🎨 COLOR GRADING
   Brightness: +5%
   Contrast: +10%
   
5. 🌫️ GRAIN OVERLAY
   Subtle noise 3% (algorithm bypass)
   
6. 📝 SUBTITLE (Optional)
   Yellow text at bottom
   Black background box
```

### Expert Mode (Selective)
```
You can enable/disable:
✓ Crop & Zoom 1.1x
✓ Speed 1.1x
✓ Mirror/Flip
✓ Mute Audio

Other effects (grading, grain) always applied
```

---

## 🎯 Class Structure (BEM)

For CSS customization, classes follow BEM naming:

```css
/* Block: reup-upload */
.reup-upload {}
.reup-upload__zone {}
.reup-upload__icon {}
.reup-upload__input {}
.reup-upload__file-name {}

/* Block: reup-settings */
.reup-settings {}
.reup-settings__title {}
.reup-settings__auto {}
.reup-settings__expert {}

/* Block: reup-card */
.reup-card {}
.reup-card__icon {}
.reup-card__text {}
.reup-card__toggle {}

/* Block: reup-content */
.reup-content {}
.reup-content__label {}
.reup-content__textarea {}

/* Block: reup-progress */
.reup-progress {}
.reup-progress__bar {}
.reup-progress__fill {}

/* Block: reup-button */
.reup-button {}
.reup-button--primary {}
.reup-button--secondary {}
```

Easy to override with custom CSS!

---

## 🔧 Backend Architecture

### Services

**SingleVideoReupService** (New)
```java
public String processVideoReup(
    VideoReupJob job,
    Path inputPath,
    boolean applyCrop,
    boolean applySpeed,
    boolean applyMirror,
    boolean removeAudio,
    String subtitle
)
```

### Controller Endpoints

**ModernReupController** (New)
```
GET  /reup/modern              Display UI
POST /reup/upload              Upload + validate
POST /reup/process/{jobId}     Start processing
GET  /reup/status/{jobId}      Poll progress
GET  /reup/download/{jobId}/0  Download video
```

### Job Flow
```
1. Upload video → Create VideoReupJob
2. Options saved (auto or manual)
3. Processing starts (async background)
4. Progress updates via polling
5. Download link provided on completion
```

---

## 📊 FFmpeg Filters Applied

### Video Filters
```bash
# Trim 0.5s from start
trim=start=0.5

# Crop & Zoom 1.1x
scale=iw*1.1:ih*1.1,crop=...

# Mirror (optional)
hflip

# Color Grading
eq=brightness=0.05:contrast=1.10:saturation=1.0

# Grain/Noise
rgbnoise=c0=0.03:c1=0.03:c2=0.03

# Subtitle (optional)
drawtext=fontfile=arial.ttf:text='...':fontsize=24:...
```

### Audio Filters
```bash
# Speed 1.1x (optional)
atempo=1.1
```

---

## 💻 Development Notes

### Adding New Transformations

Edit `SingleVideoReupService.java`:

```java
// In buildReupCommand() method:
if (enableNewFeature) {
    filterChain.append("your_new_filter,");
}
```

### Modifying UI

Edit `reup_modern.html`:

```html
<!-- Add new card to expert mode -->
<div class="reup-card" data-option="new-feature">
    <div class="reup-card__icon"><i class="fas fa-icon"></i></div>
    <div class="reup-card__text">
        <div class="reup-card__title">Feature Name</div>
        <div class="reup-card__description">Description</div>
    </div>
    <button class="reup-card__toggle" data-option="new-feature"></button>
</div>
```

### Changing Colors

Edit CSS variables in `reup_modern.html`:

```css
:root {
    --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    --success-color: #48bb78;
    /* ... */
}
```

---

## 🎬 Usage Examples

### Example 1: Quick Re-up (Auto Mode)
1. Visit `http://localhost:8080/reup/modern`
2. Upload video
3. Click "BẮT ĐẦU RE-UP"
4. Wait for completion
5. Click "TẢI XUỐNG VIDEO"

### Example 2: Custom Re-up (Expert Mode)
1. Visit URL
2. Upload video
3. Turn OFF "Auto Mode"
4. Toggle desired effects:
   - ✓ Crop & Zoom (ON)
   - ✓ Speed (ON)
   - ☐ Mirror (OFF)
   - ☐ Mute Audio (OFF)
5. Add optional subtitle
6. Click "BẮT ĐẦU RE-UP"
7. Download when complete

---

## 📱 Responsive Design

```
Desktop (> 1200px):   Full layout
Tablet (768-1200px):  Optimized spacing
Mobile (< 768px):     Single column, touch-friendly
```

---

## ✅ Checklist

- [x] Single video output (no 3 variants)
- [x] Modern, minimal UI
- [x] Auto mode (default)
- [x] Expert mode (toggle)
- [x] 4 expert options (crop, speed, mirror, audio)
- [x] Subtitle input
- [x] Real-time progress
- [x] Download functionality
- [x] BEM CSS classes
- [x] Responsive design
- [x] All transformations working
- [x] Backend service created
- [x] Controller endpoints ready
- [x] Compilation successful

---

## 🚀 Quick Start

```bash
# 1. Start server
.\mvnw.cmd spring-boot:run

# 2. Open browser
# http://localhost:8080/reup/modern

# 3. Upload video and click "BẮT ĐẦU RE-UP"

# Done! 🎉
```

---

## 📞 Support

**Need to customize?**
- Edit CSS: Search for `reup-` classes in `reup_modern.html`
- Add options: Modify `SingleVideoReupService.java`
- Change colors: Update CSS variables in `:root`

**Something broken?**
- Check browser console (F12)
- Check server logs in terminal
- Verify FFmpeg is installed at `C:\ffmpeg\ffmpeg.exe`

---

**Version**: 2.0 Modern Re-up Tool  
**Status**: ✅ Production Ready  
**Date**: 2026-04-14  

**Enjoy the new UI! 🎬✨**

