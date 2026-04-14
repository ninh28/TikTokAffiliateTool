# 🔄 Video Re-up Tool - Data Flow & Visual Guides

## 📊 Complete Data Flow Diagram

```
┌──────────────────────────────────────────────────────────────────────────┐
│                         USER UPLOADS VIDEO                              │
│  Browser → Drag & Drop → reup.html → FormData(video + options)         │
└──────────────────────────────────────────────────────────────────────────┘
                                    ↓
┌──────────────────────────────────────────────────────────────────────────┐
│                         HTTP POST /reup/upload                           │
│  FormData transmitted to backend with user-selected options             │
└──────────────────────────────────────────────────────────────────────────┘
                                    ↓
┌──────────────────────────────────────────────────────────────────────────┐
│                    ReupController.uploadVideo()                          │
│  ┌──────────────────────────────────────────────────────────────────┐   │
│  │ 1. Validate file (format, size)                                │   │
│  │ 2. Save to temp directory                                      │   │
│  │ 3. Create VideoReupJob object                                  │   │
│  │ 4. Store options (mirror, crop, speed, etc.)                  │   │
│  │ 5. Return jobId to browser                                     │   │
│  └──────────────────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────────────────┘
                                    ↓
        ┌───────────────────────────┴───────────────────────────┐
        │                                                        │
   Job Status: UPLOADING                               Job stored in memory
   Progress: 0%                                        with all options
        │                                                        │
        ↓                                                        ↓
┌──────────────────────────────────────────────────────────────────────────┐
│   Browser Response: { success: true, jobId: "uuid-1234", ... }          │
│   Display jobId to user, show progress section                           │
└──────────────────────────────────────────────────────────────────────────┘
                                    ↓
┌──────────────────────────────────────────────────────────────────────────┐
│              USER CLICKS "Bắt Đầu Xử Lý" (Start Processing)             │
│  Browser → POST /reup/process/{jobId} → Backend                         │
└──────────────────────────────────────────────────────────────────────────┘
                                    ↓
┌──────────────────────────────────────────────────────────────────────────┐
│              ReupController.processJob(jobId)                            │
│  1. Get job from ReupJobService                                         │
│  2. Start async background thread                                       │
│  3. Return immediately to browser                                       │
└──────────────────────────────────────────────────────────────────────────┘
                                    ↓
                    ┌───────────────────────────┐
                    │   Async Processing Thread │
                    └───────────────────────────┘
                                    ↓
┌──────────────────────────────────────────────────────────────────────────┐
│     ReupJobService.processReupJob(jobId)                                 │
│  1. Update status → QUEUED                                              │
│  2. Update progress → 10%                                               │
│  3. Get video file from temp storage                                    │
│  4. Call ReupVariationEngine.generateVariations()                       │
└──────────────────────────────────────────────────────────────────────────┘
                                    ↓
         ┌──────────────────────────┴──────────────────────────┐
         │                                                     │
    ┌────▼────┐  ┌────▼────┐  ┌────▼────┐  ┌────▼────┐       │
    │Variant 1│  │Variant 2│  │Variant 3│  │  TextO  │       │
    │   V1    │  │   V2    │  │   V3    │  │ Services│       │
    └─────────┘  └─────────┘  └─────────┘  └─────────┘       │
         │            │            │            │             │
         ▼            ▼            ▼            ▼             │
    ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌────────┐        │
    │  Aspect │  │ Filters │  │ Content │  │ Text   │        │
    │ Ratio   │  │ Effects │  │ Edit    │  │ Overlay│        │
    │ Service │  │ Service │  │ Service │  │Service │        │
    └─────────┘  └─────────┘  └─────────┘  └────────┘        │
         │            │            │            │             │
         └────────────┴────────────┴────────────┘             │
                      ↓                                        │
         ┌────────────────────────────────────┐               │
         │  ReupVariationEngine orchestrates   │               │
         │  FFmpeg command building           │               │
         └────────────────────────────────────┘               │
                      ↓                                        │
         ┌────────────────────────────────────┐               │
         │ Build 3 FFmpeg command chains:     │               │
         │ - V1: Mirror + Crop + Brightness  │               │
         │ - V2: Vibrant colors + Saturation │               │
         │ - V3: Aggressive crop + Speed     │               │
         └────────────────────────────────────┘               │
                      ↓                                        │
         ┌────────────────────────────────────┐               │
         │  Execute FFmpeg via ProcessBuilder │               │
         │  C:\ffmpeg\ffmpeg.exe [args...]    │               │
         └────────────────────────────────────┘               │
                      ↓                                        │
    ┌──────────────────┬──────────────────┬──────────────────┐  │
    │                  │                  │                  │  │
    ▼                  ▼                  ▼                  ▼  │
  V1.mp4            V2.mp4             V3.mp4           Update  │
  Saved to         Saved to            Saved to          Progress│
  storage/reup/    storage/reup/       storage/reup/      to 100%│
```

---

## 🔄 Variation Generation Details

### Variation 1: Aspect Ratio Focus 🎯

```
INPUT VIDEO
    ↓
┌───────────────────────────────────────────┐
│ AspectRatioTransformService               │
│ .buildZoomMirrorCommand()                 │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Filter Chain:
 └─ [hflip] (mirror/horizontal flip)
 └─ [scale] (zoom 95% of original)
    ↓
┌───────────────────────────────────────────┐
│ FilterEffectsService                      │
│ .buildColorAdjustCommand()                │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Filter Chain:
 └─ [eq] brightness=0.05:contrast=1.0
    ↓
┌───────────────────────────────────────────┐
│ ContentEditService                        │
│ .buildSpeedCommand()                      │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Audio Filter:
 └─ [atempo] speed multiplier (1.0-2.0)
    ↓
OUTPUT: [jobId]_v1.mp4 ✓
```

### Variation 2: Filter & Effects Focus 🌈

```
INPUT VIDEO
    ↓
┌───────────────────────────────────────────┐
│ FilterEffectsService                      │
│ .buildVibrantCommand()                    │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Filter Chain:
 └─ [eq] brightness=0.1:contrast=1.2:saturation=1.3
    ↓
┌───────────────────────────────────────────┐
│ AspectRatioTransformService               │
│ .buildMirrorCommand()                     │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Filter Chain:
 └─ [hflip] (if enabled)
 └─ [crop] 90% (if enabled)
    ↓
┌───────────────────────────────────────────┐
│ ContentEditService                        │
│ .buildSpeedCommand()                      │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Audio Filter:
 └─ [atempo] speed multiplier
    ↓
OUTPUT: [jobId]_v2.mp4 ✓
```

### Variation 3: Content Edit & Text Focus ⚡

```
INPUT VIDEO
    ↓
┌───────────────────────────────────────────┐
│ ContentEditService                        │
│ .buildRandomCropCommand()                 │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Filter Chain:
 └─ [crop] 85% aggressive framing
    ↓
┌───────────────────────────────────────────┐
│ FilterEffectsService                      │
│ .buildColorAdjustCommand()                │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Filter Chain:
 └─ [eq] brightness=0.02:contrast=1.15:saturation=1.1
    ↓
┌───────────────────────────────────────────┐
│ AspectRatioTransformService               │
│ .buildMirrorCommand()                     │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Filter Chain:
 └─ [vflip] (vertical flip if enabled)
    ↓
┌───────────────────────────────────────────┐
│ ContentEditService                        │
│ .buildSpeedCommand()                      │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Audio Filter:
 └─ [atempo] 1.21 (20% faster than user speed)
    ↓
┌───────────────────────────────────────────┐
│ TextOverlayService                        │
│ .addTextOverlays()                        │
└───────────────────────────────────────────┘
    ↓
 FFmpeg Filter Chain:
 └─ [drawtext] watermark + subtitle (if enabled)
    ↓
OUTPUT: [jobId]_v3.mp4 ✓
```

---

## 🌊 Browser Status Polling Flow

```
While Job is Processing:

Browser (Every 2 seconds):
  ↓
GET /reup/status/{jobId}
  ↓
┌──────────────────────────────────┐
│ ReupJobService.getJob(jobId)    │
│ Return current job state        │
└──────────────────────────────────┘
  ↓
┌──────────────────────────────────┐
│ Response JSON:                   │
│ {                                │
│   status: "PROCESSING",          │
│   progress: 33,                  │
│   message: "Đang xử lý (33%)..." │
│ }                                │
└──────────────────────────────────┘
  ↓
Browser:
  ├─ Update progress bar: 33%
  ├─ Update message
  ├─ Check if status == COMPLETED
  └─ If not → Wait 2 sec → Poll again

When Processing Done:

Response:
┌──────────────────────────────────┐
│ {                                │
│   status: "COMPLETED",           │
│   progress: 100,                 │
│   outputVideos: [                │
│     "path/to/jobId_v1.mp4",      │
│     "path/to/jobId_v2.mp4",      │
│     "path/to/jobId_v3.mp4"       │
│   ]                              │
│ }                                │
└──────────────────────────────────┘
  ↓
Browser:
  ├─ Stop polling
  ├─ Hide progress section
  ├─ Show results gallery
  └─ Display 3 videos with download buttons
```

---

## 📥 Download Flow

```
User clicks "Tải Xuống" on Variation 1:
  ↓
GET /reup/download/{jobId}/0
  ↓
┌──────────────────────────────────────────┐
│ ReupController.downloadVariant()         │
│ 1. Get job by jobId                      │
│ 2. Get output path from job.outputVideos[0] │
│ 3. Read MP4 file into byte[]             │
└──────────────────────────────────────────┘
  ↓
HTTP Response:
┌──────────────────────────────────────────┐
│ Headers:                                 │
│ Content-Type: video/mp4                  │
│ Content-Disposition: attachment;         │
│   filename="reup_[jobId]_v1.mp4"        │
│                                          │
│ Body: Binary MP4 data                    │
└──────────────────────────────────────────┘
  ↓
Browser:
  ├─ Receives binary data
  ├─ Treats as downloadable file
  └─ Saves to Downloads folder
```

---

## 🔐 Job State Transitions

```
┌──────────┐
│ UPLOADING│
└────┬─────┘
     │ (File saved to disk)
     ↓
┌──────────┐
│  QUEUED  │
└────┬─────┘
     │ (Processing starts)
     ↓
┌──────────────┐
│  PROCESSING  │
└────┬─────────┘
     │ (Progress: 0% → 100%)
     ├─────────────────┬──────────────────┐
     │ V1 Done         │ V2 Done          │ V3 Done
     │ Progress: 33%   │ Progress: 66%    │ Progress: 90%
     │                 │                  │
     ↓                 ↓                  ↓
┌──────────────┐
│  COMPLETED   │
└──────────────┘
 (All 3 videos saved)

OR if Error Occurs:

┌──────────┐
│  ERROR   │
└──────────┘
 (Job status = ERROR)
 (Error message set)
```

---

## 💾 File Storage Flow

```
1. UPLOAD → TEMP
   temp_uploads/reup/
   └─ [timestamp]_original_filename.mp4
   
2. PROCESSING → OUTPUT
   src/main/resources/static/videos/reup/
   └─ [jobId]_v1.mp4
   └─ [jobId]_v2.mp4
   └─ [jobId]_v3.mp4
   
3. DOWNLOAD
   Browser downloads:
   reup_[jobId]_v1.mp4
   reup_[jobId]_v2.mp4
   reup_[jobId]_v3.mp4
```

---

## 🎛️ Option Selection Flow

```
User toggles options in browser:
  ↓
JavaScript tracks toggle state:
  ├─ applyMirror: true/false
  ├─ applyCrop: true/false
  ├─ speedMultiplier: 1.0-2.0
  ├─ addSubtitle: true/false
  ├─ subtitleText: "..."
  ├─ addFilter: true/false
  ├─ addWatermark: true/false
  └─ watermarkText: "..."
  ↓
FormData includes all options:
  ↓
POST /reup/upload with FormData
  ↓
ReupController receives options:
  ↓
VideoReupJob stores options:
  ↓
ReupVariationEngine uses options:
  ├─ V1: Apply selected options + aspect ratio focus
  ├─ V2: Apply selected options + filter focus
  └─ V3: Apply selected options + content focus
```

---

## 🔄 Error Handling Flow

```
Error occurs during processing:
  ↓
Try-catch catches exception:
  ↓
ReupJobService.setJobError(jobId, message):
  ├─ Set status = ERROR
  ├─ Set errorMessage = message
  └─ Log error to console
  ↓
Browser polling gets error status:
  ↓
Response includes error message:
  └─ { status: "ERROR", error: "..." }
  ↓
Browser displays error:
  ├─ Show error message
  ├─ Hide progress bar
  ├─ Enable upload button
  └─ Allow retry
```

---

## 📊 Performance Timeline

```
Upload Video (1 minute):
  ├─ 0s: User selects file
  ├─ 1s: Upload starts (30 sec for 500MB)
  ├─ 31s: Upload complete, job created
  └─ Status: QUEUED

Process Video:
  ├─ 31s: User clicks "Bắt Đầu Xử Lý"
  ├─ 32s: Processing starts in background
  │        Progress: 10%
  ├─ 40s: Variation 1 complete
  │        Progress: 35%
  ├─ 50s: Variation 2 complete
  │        Progress: 70%
  ├─ 60s: Variation 3 + text complete
  │        Progress: 100%
  └─ Status: COMPLETED

Download:
  ├─ 60s: User clicks "Tải Xuống"
  ├─ 61s: Browser downloads V1.mp4
  ├─ 62s: User clicks "Tải Xuống"
  ├─ 63s: Browser downloads V2.mp4
  ├─ 64s: User clicks "Tải Xuống"
  └─ 65s: Browser downloads V3.mp4
```

---

## 🎯 Key Flow Points

1. **Upload**: File → FormData → POST → Temp Storage
2. **Job Creation**: Temp File → VideoReupJob → Store
3. **Processing**: Job → Services → FFmpeg → V1, V2, V3
4. **Status**: Poll → ReupJobService → Return state
5. **Download**: Output → Byte Stream → Browser Download

---

**Visual Reference**: Use this guide to understand how data flows through the system!

