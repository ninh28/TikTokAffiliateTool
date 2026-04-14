# ✅ VERIFICATION CHECKLIST - Kiểm Tra Toàn Bộ

## 🏗️ Build & Compile

### **Step 1: Clean & Compile**
```powershell
cd C:\Users\admin\IdeaProjects\TikTokAffiliateTool
mvn clean compile -q
```

**Expected Output**: No errors, build success

**Check**:
- [ ] No compilation errors
- [ ] No warning messages
- [ ] Command completes in < 2 minutes

---

### **Step 2: Package (JAR)**
```powershell
mvn clean package -DskipTests -q
```

**Expected Output**: JAR file created

**Check**:
- [ ] `target/TikTokAffiliateTool-*.jar` exists
- [ ] File size > 50MB
- [ ] No packaging errors

---

### **Step 3: Run Application**
```powershell
java -jar target/TikTokAffiliateTool-*.jar
```

**Expected Output**:
```
...
2026-04-14 10:30:00 INFO  ... TomcatWebServer : Tomcat started on port(s): 8080
2026-04-14 10:30:01 INFO  ... TikTokAffiliateToolApplication : Started...
```

**Check**:
- [ ] No errors in startup
- [ ] Server listening on port 8080
- [ ] No exception traces

---

## 🌐 URL & Routing

### **Test 1: Main Page**
```
URL: http://localhost:8080
Expected: Redirect to index page
Check: [ ] Page loads
```

### **Test 2: Old UI (Backup)**
```
URL: http://localhost:8080/reup/modern-old
Expected: Old giao diện (reup_modern_clean.html)
Check: [ ] Page loads with light theme
```

### **Test 3: New UI (Main)**
```
URL: http://localhost:8080/reup/modern
Expected: New giao diện dark (reup_new.html)
Check: [ ] Page loads with dark theme ✅
```

---

## 🎨 Frontend Tests

### **Test 4: UI Elements Load**
Access `http://localhost:8080/reup/modern` and check:

- [ ] Header "TikTok Re-up Tool" visible
- [ ] Drop-zone with cloud icon visible
- [ ] "Auto Re-up" toggle visible
- [ ] 4 setting cards visible (Expert mode)
- [ ] Subtitle textarea visible
- [ ] Progress bar hidden (initially)
- [ ] "BẮT ĐẦU RE-UP" button visible and enabled=false
- [ ] "Tải lại" button visible

### **Test 5: Auto Mode Toggle**
- [ ] Click toggle → Card appears
- [ ] Click again → Card disappears
- [ ] Animation smooth (0.3s)

### **Test 6: Expert Cards**
- [ ] Click on card → Toggle changes
- [ ] Toggle shows color change (gray → gradient)
- [ ] 4 cards functional (Crop, Speed, Mirror, Audio)

### **Test 7: File Upload**
- [ ] Click drop-zone → File dialog opens
- [ ] Drag video into zone → Accepted
- [ ] Select valid file → File name shows
- [ ] File size shows correctly
- [ ] "BẮT ĐẦU RE-UP" button enabled

### **Test 8: File Validation**
- [ ] Try MP4 → Accepted ✅
- [ ] Try MOV → Accepted ✅
- [ ] Try AVI → Accepted ✅
- [ ] Try MKV → Accepted ✅
- [ ] Try TXT → Error alert ❌
- [ ] Try > 500MB file → Error alert ❌

---

## 📡 API Endpoints

### **Test 9: Upload Endpoint**
```
POST /reup/api/upload
Content-Type: multipart/form-data

Parameters:
- video: <file>
- subtitle: "test"
- isAutoMode: true
- applyCrop: true
- applySpeed: true
- applyMirror: false
- removeAudio: false
```

**Expected Response**:
```json
{
  "success": true,
  "jobId": "job_1234567890",
  "message": "✅ Video uploaded successfully"
}
```

**Check**:
- [ ] Status 200 OK
- [ ] Response contains jobId
- [ ] Video saved to temp folder

### **Test 10: Process Endpoint**
```
POST /reup/api/process/{jobId}
```

**Expected Response**:
```json
{
  "success": true,
  "jobId": "job_1234567890",
  "message": "🚀 Đang xử lý video..."
}
```

**Check**:
- [ ] Status 200 OK
- [ ] Processing starts in background

### **Test 11: Status Endpoint (Both Paths)**
```
GET /reup/api/status/{jobId}
GET /reup/api/job-status/{jobId}  ← NEW
```

**Expected Response (Processing)**:
```json
{
  "success": true,
  "jobId": "job_1234567890",
  "status": "PROCESSING",
  "progressPercent": 45,
  "fileName": "video.mp4",
  "message": "⏳ Đang xử lý (45%)..."
}
```

**Expected Response (Completed)**:
```json
{
  "success": true,
  "jobId": "job_1234567890",
  "status": "COMPLETED",
  "progressPercent": 100,
  "fileName": "video.mp4",
  "outputPath": "src/main/resources/static/videos/reup/job_1234567890_reup.mp4",
  "message": "✅ Xử lý hoàn tất!"
}
```

**Check**:
- [ ] Status endpoint works
- [ ] Job-status endpoint works
- [ ] Both return same format
- [ ] progressPercent increases
- [ ] outputPath provided when completed

---

## 🎬 E2E Workflow Test

### **Test 12: Complete Flow (2-5 minutes)**

**Setup**:
- Prepare a test video MP4 file (50-150MB, 1-3 min duration)

**Steps**:
1. [ ] Open `http://localhost:8080/reup/modern`
2. [ ] Upload test video
3. [ ] Check Auto Mode is ON
4. [ ] Leave subtitle empty
5. [ ] Click "BẮT ĐẦU RE-UP"
6. [ ] Watch progress bar increase (should update every 2s)
7. [ ] Wait for completion (2-5 minutes)
8. [ ] See result section with preview
9. [ ] Click "Tải video" button
10. [ ] Check file downloads
11. [ ] Verify file exists and playable
12. [ ] Verify it's **1 video only** (not 3)

**Check**:
- [ ] Alert messages appear correctly
- [ ] Progress bar updates
- [ ] Video completes processing
- [ ] Result section shows
- [ ] Video download works
- [ ] Downloaded file playable

---

## 🔍 Code Verification

### **Test 13: File Checks**

**1. HTML Template**
```
File: src/main/resources/templates/reup_new.html
Check:
  [ ] File exists
  [ ] Size ~ 900 lines
  [ ] Contains: reup-container, dropZone, autoToggle, expertMode
  [ ] JavaScript inline included
  [ ] No external CSS (CSS in <style> tag)
```

**2. Java Controller**
```
File: src/main/java/com/ninhquachhai/tiktoktool/controller/ModernReupController.java
Check:
  [ ] File exists
  [ ] Route /reup/modern → reup_new
  [ ] Route /reup/modern-old → reup_modern_clean
  [ ] @GetMapping({"/api/status/{jobId}", "/api/job-status/{jobId}"})
  [ ] outputPath in response
  [ ] No Path conversion errors
```

**3. Service Layer**
```
File: src/main/java/com/ninhquachhai/tiktoktool/service/SingleVideoReupService.java
Check:
  [ ] File exists
  [ ] Tạo 1 video duy nhất
  [ ] Implements all transformations:
      [ ] Trim 0.5s head/tail
      [ ] Zoom 1.1x
      [ ] Mirror (optional)
      [ ] Color grading
      [ ] Noise overlay
      [ ] Subtitle (optional)
      [ ] Speed 1.1x
      [ ] Audio handling
```

---

## 📊 Database/Storage Checks

### **Test 14: File System**
```
Check directories:
[ ] temp_uploads/modern/ - Video uploads saved here
[ ] src/main/resources/static/videos/reup/ - Output videos
[ ] Check files created: job_xxxxx_reup.mp4
```

### **Test 15: Output Video Verification**

Use FFprobe to check:
```powershell
# Check video properties
ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1:nokey=1 output.mp4

# Check codec
ffprobe -v error -select_streams v:0 -show_entries stream=codec_name,width,height,r_frame_rate output.mp4

# Check if zoom applied (resolution different)
# Original: 1080x1920
# Output: Should show evidence of zoom/transform
```

**Check**:
- [ ] Output video exists
- [ ] Output video playable
- [ ] Output video duration shorter (trim applied)
- [ ] Quality maintained
- [ ] Only 1 output video (not 3)

---

## 🚨 Error Handling Tests

### **Test 16: Error Scenarios**

**Scenario 1: Invalid File Format**
- [ ] Try upload .txt file
- [ ] Error message appears
- [ ] Button disabled

**Scenario 2: File Too Large**
- [ ] Create file > 500MB
- [ ] Try upload
- [ ] Error message appears

**Scenario 3: Missing FFmpeg**
- [ ] Stop FFmpeg service
- [ ] Try process video
- [ ] Error message in response
- [ ] User informed

**Scenario 4: Network Error**
- [ ] Disable internet (simulate)
- [ ] Try upload
- [ ] Error handled gracefully

**Scenario 5: Processing Timeout**
- [ ] Wait > 10 minutes
- [ ] Should show timeout error
- [ ] Can retry

---

## 💻 Browser Compatibility

### **Test 17: Cross-Browser**
Test on:
- [ ] Chrome/Chromium (Latest)
- [ ] Firefox (Latest)
- [ ] Edge (Latest)
- [ ] Safari (if Mac available)

**Check**:
- [ ] Giao diện loads
- [ ] Dark theme applied
- [ ] All animations work
- [ ] No console errors (F12 → Console)
- [ ] File upload works
- [ ] Progress bar animates

---

## 📱 Responsive Design Tests

### **Test 18: Mobile View**
```
Open DevTools (F12) → Device Emulation

Test sizes:
[ ] 375px (iPhone SE) - Portrait
[ ] 768px (iPad) - Portrait
[ ] 1024px (Tablet) - Landscape
[ ] 1920px (Desktop) - Full
```

**Check**:
- [ ] Layout adapts
- [ ] Text readable
- [ ] Buttons touchable (min 44x44px)
- [ ] No horizontal scroll
- [ ] Images responsive

---

## 📊 Performance Tests

### **Test 19: Load Time**
```powershell
# Measure time to first paint
# Open DevTools → Network tab → Reload page

Expected:
  - TTFB: < 200ms
  - FCP: < 500ms
  - LCP: < 1s
  - Total load: < 3s
```

**Check**:
- [ ] Page loads quickly
- [ ] No visual jank
- [ ] Animations smooth (60fps)

### **Test 20: Upload Performance**
```
Test file: 100MB MP4

Expected:
  - Upload time: < 30s (fast internet)
  - Processing time: 2-5 minutes
  - Total: < 10 minutes
```

---

## 📋 Final Checklist

### **Pre-Deployment**
- [ ] Code compiles without errors
- [ ] All URLs work
- [ ] UI looks correct
- [ ] All API endpoints responsive
- [ ] File upload works
- [ ] Video processing works
- [ ] Progress tracking works
- [ ] Download works
- [ ] E2E workflow complete
- [ ] Errors handled gracefully
- [ ] Mobile responsive
- [ ] Cross-browser compatible

### **Production Ready**
- [ ] Performance acceptable
- [ ] Security OK (file validation)
- [ ] FFmpeg installed & configured
- [ ] Temp folders writable
- [ ] Enough disk space (> 1GB free)
- [ ] No hardcoded paths
- [ ] Logs configured
- [ ] Error notifications working

### **Post-Deployment**
- [ ] Monitor server logs
- [ ] Check disk usage
- [ ] Verify output quality
- [ ] User feedback collected
- [ ] Performance metrics tracked

---

## 🎯 Success Criteria

**All tests passed?** ✅ READY FOR PRODUCTION

**Some tests failed?** ⚠️ DEBUG & FIX

### **Quick Fix Guide**

| Issue | Solution |
|-------|----------|
| Compilation error | Check Java version (11+), Maven installed |
| Page not loading | Check server running (port 8080), firewall |
| Upload failed | Check file format, size, disk space |
| Processing failed | Check FFmpeg installed, path correct |
| Download failed | Check file exists, permissions, disk space |
| UI looks broken | Clear browser cache (Ctrl+Shift+Delete) |
| Progress stuck | Refresh page, check server logs |

---

## 📝 Sign-Off

**Test Date**: _______________  
**Tester**: _______________  
**Result**: ✅ PASS / ⚠️ FAIL / ❌ ERROR  
**Notes**: _______________

---

**Verification Guide Version**: 2.0  
**Last Updated**: 2026-04-14  
**Total Tests**: 20

