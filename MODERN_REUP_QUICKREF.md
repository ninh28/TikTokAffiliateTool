# 🎯 Modern Re-up Tool - Quick Reference

## Access URLs

```
Modern UI:        http://localhost:8080/reup/modern
API Upload:       POST /reup/upload
API Process:      POST /reup/process/{jobId}
API Status:       GET /reup/status/{jobId}
API Download:     GET /reup/download/{jobId}/0
```

---

## File Locations

```
Frontend:    src/main/resources/templates/reup_modern.html
Service:     src/main/java/.../service/SingleVideoReupService.java
Controller:  src/main/java/.../controller/ModernReupController.java
Guide:       MODERN_REUP_GUIDE.md (this project root)
```

---

## UI Blocks

| Block | Purpose | Contents |
|-------|---------|----------|
| **1** | Upload | Drop zone, file selector |
| **2** | Settings | Auto toggle, expert cards |
| **3** | Content | Subtitle input, progress bar |
| **CTA** | Action | Start/Download button |

---

## Auto Mode vs Expert Mode

### Auto Mode (Default) ✅
```
✓ Trim 0.5s edges
✓ Crop & Zoom 1.1x
✓ Speed 1.1x
✓ Color grading
✓ Grain overlay
```

### Expert Mode 🔧
```
Manually toggle:
☐ Crop & Zoom
☐ Speed 1.1x
☐ Mirror Flip
☐ Mute Audio

Always applied:
✓ Color grading
✓ Grain overlay
```

---

## Transformations

| Effect | Parameter | Purpose |
|--------|-----------|---------|
| Trim | 0.5s start + end | Remove artifacts |
| Zoom | 1.1x scale | Change composition |
| Speed | 1.1x tempo | More dynamic |
| Color | +5% brightness | Natural look |
| Grain | 3% noise | Algorithm bypass |
| Mirror | hflip | Different angle |
| Audio | 100% volume | Keep or remove |
| Subtitle | Yellow text | Custom overlay |

---

## CSS Class Naming (BEM)

```css
/* Block__Element--Modifier */
.reup-upload__zone
.reup-card--primary
.reup-button--primary
.reup-progress.show
```

Easy to override with custom CSS!

---

## Processing Flow

```
1. User uploads video
   ↓
2. Auto/Expert options saved
   ↓
3. Async background processing
   ↓
4. Progress polls every 1 sec
   ↓
5. On completion: Download link
```

---

## API Response Format

### Upload Success
```json
{
    "success": true,
    "jobId": "uuid-1234",
    "message": "✅ Video uploaded successfully"
}
```

### Status Update
```json
{
    "success": true,
    "status": "PROCESSING",
    "progress": 45,
    "message": "⏳ Đang xử lý (45%)..."
}
```

### Complete
```json
{
    "success": true,
    "status": "COMPLETED",
    "progress": 100,
    "outputVideos": ["/videos/reup/..."],
    "message": "✅ Xử lý hoàn tát!"
}
```

---

## Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Upload fails | Check file format & size (< 500MB) |
| Processing slow | Video too long, try < 2 min |
| FFmpeg not found | Install at `C:\ffmpeg\ffmpeg.exe` |
| Download broken | Check output folder exists |
| Progress not updating | Browser cache, try Ctrl+Shift+R |

---

## Key Features

✅ **1 Video In, 1 Video Out**  
✅ **Modern Minimalist UI**  
✅ **Auto Mode (Recommended)**  
✅ **Expert Mode (Control)**  
✅ **Real-time Progress**  
✅ **BEM CSS Classes**  
✅ **Mobile Responsive**  
✅ **Gradient Buttons**  
✅ **Dark Theme**  
✅ **Status Alerts**  

---

## Customization Tips

### Change Primary Color
```css
:root {
    --primary-gradient: linear-gradient(135deg, #YOUR_COLOR1 0%, #YOUR_COLOR2 100%);
}
```

### Add New Expert Option
1. Add card in `reup_modern.html` expert mode
2. Add JavaScript toggle handler
3. Update `ModernReupController` to handle option
4. Update `SingleVideoReupService` to apply effect

### Modify Transformations
Edit `SingleVideoReupService.buildReupCommand()`:
```java
if (yourOption) {
    filterChain.append("your_filter,");
}
```

---

## Performance

| Video Length | Time | Status |
|------------|------|--------|
| 30 sec | 10-20s | ✅ Fast |
| 1 min | 20-40s | ✅ Good |
| 2 min | 40-60s | ⚠️ Slow |
| 5+ min | > 100s | ❌ Very Slow |

---

## Browser Support

| Browser | Desktop | Mobile |
|---------|---------|--------|
| Chrome | ✅ | ✅ |
| Firefox | ✅ | ✅ |
| Safari | ✅ | ✅ |
| Edge | ✅ | ✅ |

---

## Files Modified/Created

```
NEW:
├── reup_modern.html              (UI)
├── SingleVideoReupService.java    (Backend)
├── ModernReupController.java      (API)
└── MODERN_REUP_GUIDE.md           (Docs)

EXISTING:
└── VideoReupJob.java              (Already has fields)
```

---

## Start Command

```bash
.\mvnw.cmd spring-boot:run
```

Then visit: `http://localhost:8080/reup/modern`

---

**Status**: ✅ Complete  
**Version**: 2.0  
**Type**: Modern Single-Video Re-up Tool  

🎬 Ready to use!

