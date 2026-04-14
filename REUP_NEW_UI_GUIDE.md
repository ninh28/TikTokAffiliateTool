# 🎬 TikTok Re-up Tool - Hướng Dẫn Sử Dụng Giao Diện Mới

## 📋 Tóm Tắt Thay Đổi

### 1. **Giao Diện Mới - `reup_new.html`**
   - ✅ **Dark Mode** hoàn toàn (Background #121212)
   - ✅ **Bố cục 3 khối** rõ ràng:
     - Khối 1: Upload (Drop-zone)
     - Khối 2: Settings (Auto/Expert mode)
     - Khối 3: Content (Subtitle)
   - ✅ **Hiệu ứng hiện đại**:
     - Gradient tím-xanh (Neon #8B5CF6 + Cyan #06B6D4)
     - Hover effects trên các thành phần
     - Animations mượt mà (float, spin, pulse)
   - ✅ **Toggle switches** kiểu iOS
   - ✅ **Progress bar** hiển thị theo thời gian thực
   - ✅ **Responsive design** (Mobile + Desktop)

### 2. **Xử Lý Chỉ 1 Video**
   - ✅ `SingleVideoReupService` tạo **1 video duy nhất** (không phải 3)
   - ✅ Áp dụng công thức re-up hoàn chỉnh:
     - Cắt bỏ 1s đầu + 1s cuối
     - Zoom 1.1x
     - Tăng tốc độ 1.1x
     - Lật mirror (tùy chọn)
     - Điều chỉnh màu sắc
     - Thêm phụ đề (nếu có)
     - Thêm noise/grain overlay

### 3. **Controller - `ModernReupController.java`**
   - ✅ Sửa endpoint `/api/job-status/{jobId}` (hỗ trợ cả 2: `/api/status/` và `/api/job-status/`)
   - ✅ Thêm `outputPath` vào response khi completed
   - ✅ Route `/reup/modern` → gọi `reup_new.html` (UI mới)
   - ✅ Route `/reup/modern-old` → gọi `reup_modern_clean.html` (UI cũ)
   - ✅ Không có lỗi `Path` conversion

---

## 🚀 Hướng Dẫn Sử Dụng

### **Bước 1: Truy Cập Tool**
```
URL: http://localhost:8080/reup/modern
```

### **Bước 2: Upload Video**
- **Cách 1**: Click vào drop-zone hoặc kéo thả video vào
- **Cách 2**: Chọn file MP4, MOV, AVI, MKV (Max 500MB)
- **Hiển thị**: Tên file + dung lượng

### **Bước 3: Cấu Hình Re-up**

#### **Chế độ Auto (Khuyên dùng)**
- Toggle bật **Auto Re-up**
- Tự động áp dụng:
  - ✅ Cắt xén & Zoom 1.1x
  - ✅ Tăng tốc 1.1x
  - ❌ Không lật mirror
  - ❌ Không tắt âm thanh

#### **Chế độ Expert**
- Toggle tắt **Auto Re-up**
- Tùy chỉnh từng tùy chọn:
  - 🎬 Cắt xén & Zoom: Bật/Tắt
  - ⏩ Tăng tốc 1.1x: Bật/Tắt
  - 🔄 Lật Mirror: Bật/Tắt
  - 🔇 Tắt âm thanh: Bật/Tắt

### **Bước 4: Nhập Phụ Đề (Tùy Chọn)**
- Nhập tiêu đề video hoặc nội dung mô tả
- Phụ đề sẽ được hiển thị trên video đầu ra

### **Bước 5: Bắt Đầu Re-up**
- Click nút **BẮT ĐẦU RE-UP** (Gradient tím-xanh)
- **Quá trình**:
  1. Upload video (~5-10s)
  2. Xử lý video (2-5 phút tùy dung lượng)
  3. Hiển thị progress bar (0-100%)

### **Bước 6: Tải Video**
- Khi hoàn thành, hiển thị:
  - ✅ Icon thành công
  - 📹 Video preview
  - 📥 Nút "Tải video"
- Click để tải xuống video re-up

---

## 🛠️ Quy Trình Re-up Chi Tiết

### **Công Thức Áp Dụng (Flow Cắt Lại)**

```
INPUT VIDEO
    ↓
1️⃣ TRIM (Cắt đầu/cuối)
   - Cắt bỏ 0.5s đầu
   - Cắt bỏ 0.5s cuối
    ↓
2️⃣ ZOOM & SCALE
   - Scale up 1.1x
   - Crop center (loại bỏ viền)
    ↓
3️⃣ MIRROR (Tùy chọn)
   - Lật ngang toàn bộ video
    ↓
4️⃣ COLOR GRADING
   - Brightness +5%
   - Contrast +10%
   - Saturation tự nhiên
    ↓
5️⃣ EFFECTS
   - Thêm noise/grain overlay (3% opacity)
    ↓
6️⃣ SUBTITLE (Nếu có)
   - Thêm text hook ở vị trí bottom-center
   - Màu vàng, nền đen trong suốt
    ↓
7️⃣ AUDIO
   - Giảm âm lượng gốc 10% (giữ tiếng môi trường)
   - Hoặc tắt hoàn toàn (nếu chọn)
    ↓
8️⃣ SPEED
   - Tăng tốc độ 1.1x (giữ tone giọng tự nhiên)
    ↓
OUTPUT: 1 Video Re-up Duy Nhất 🎉
```

---

## 📊 API Endpoints

### **Upload Video**
```
POST /reup/api/upload
Params:
  - video: File (MP4, MOV, AVI, MKV)
  - subtitle: String (optional)
  - isAutoMode: Boolean (default: true)
  - applyCrop: Boolean
  - applySpeed: Boolean
  - applyMirror: Boolean
  - removeAudio: Boolean

Response:
{
  "success": true,
  "jobId": "job_1234567890",
  "message": "✅ Video uploaded successfully"
}
```

### **Process Video**
```
POST /reup/api/process/{jobId}
Response:
{
  "success": true,
  "jobId": "job_1234567890",
  "message": "🚀 Đang xử lý video..."
}
```

### **Check Job Status**
```
GET /reup/api/job-status/{jobId}
or
GET /reup/api/status/{jobId}

Response:
{
  "success": true,
  "jobId": "job_1234567890",
  "status": "COMPLETED", // UPLOADING, QUEUED, PROCESSING, COMPLETED, ERROR
  "progressPercent": 100,
  "fileName": "video.mp4",
  "outputPath": "/path/to/reup_video.mp4",
  "message": "✅ Xử lý hoàn tất!"
}
```

---

## 🎨 Thiết Kế Giao Diện

### **Màu Sắc**
- **Background**: #121212 (Deep Black)
- **Card/Components**: #1E1E1E, #252525
- **Accent Primary**: #8B5CF6 (Neon Purple)
- **Accent Secondary**: #06B6D4 (Cyan)
- **Text**: #FFFFFF (White), #808080 (Gray)

### **Border Radius**
- Tất cả: 12px (Tổng quát)
- Buttons: 12px
- Cards: 12px
- Inputs: 12px

### **Animations**
- Hover effects: 0.3s ease
- Progress fill: smooth transition
- Alerts: slideDown animation
- Loading spinner: spin infinite

### **Typography**
- **Font Family**: System default (-apple-system, BlinkMacSystemFont, Segoe UI, Roboto)
- **H1 (Title)**: 48px, Bold (700)
- **H2 (Section)**: 20px, SemiBold (600)
- **Body**: 14-16px, Regular (400)

---

## 📁 File Structure

```
src/main/resources/
├── templates/
│   ├── reup_new.html          ← 🆕 Giao diện mới (sử dụng)
│   ├── reup_modern_clean.html ← Giao diện cũ (backup)
│   └── ...
│
└── static/
    └── videos/
        └── reup/              ← Output videos
            └── job_xxxxx_reup.mp4

src/main/java/.../controller/
└── ModernReupController.java  ← 🆕 Sửa endpoints

src/main/java/.../service/
└── SingleVideoReupService.java  ← Tạo 1 video duy nhất
```

---

## ⚙️ Configuration

### **FFmpeg Path** (application.properties)
```
ffmpeg.path=C:/ffmpeg/ffmpeg.exe
ffprobe.path=C:/ffmpeg/bin/ffprobe
```

### **Upload Limits**
```
spring.servlet.multipart.max-file-size=500MB
spring.servlet.multipart.max-request-size=500MB
```

### **Temp Upload Directory**
```
temp_uploads/modern/  ← Tạm thời lưu video upload
```

---

## 🔧 Troubleshooting

### **Lỗi: "Định dạng video không hỗ trợ"**
- ✅ Sử dụng: MP4, MOV, AVI, MKV, WebM
- ❌ Không hỗ trợ: TS, M3U8, WAV, GIF

### **Lỗi: "File quá lớn"**
- Max: 500MB
- Giải pháp: Compress video trước khi upload

### **Lỗi: "Job không tìm thấy"**
- Nguyên nhân: Session timeout hoặc job bị xóa
- Giải pháp: Refresh trang và upload lại

### **Xử lý quá lâu**
- Video dài → Mất thời gian
- Spec máy yếu → Tăng thời chờ
- Max timeout: 10 phút

---

## 📝 Ghi Chú

### **Khác Biệt với UI Cũ**
| Feature | Cũ | Mới |
|---------|----|----|
| Theme | Light | Dark |
| Color | Blue/Green | Purple/Cyan |
| Layout | Nested | 3-block clear |
| Cards | Compact | Spacious |
| Animations | Minimal | Rich |
| Responsive | Partial | Full |
| Video Output | 3 videos | 1 video |
| Progress | Polling | Real-time |

### **Performance Tips**
1. Upload video < 100MB cho tốc độ tối ưu
2. Tắt Expert mode nếu không biết cấu hình
3. Không refresh page khi đang xử lý
4. Kiểm tra FFmpeg đã cài đặt đúng không

---

## 🎯 Next Steps

1. **Build project**: `mvn clean package`
2. **Run application**: `java -jar target/TikTokAffiliateTool-*.jar`
3. **Access**: `http://localhost:8080/reup/modern`
4. **Test**: Upload video test, kiểm tra output

---

**Tạo bởi**: GitHub Copilot  
**Ngày**: 2026-04-14  
**Version**: 2.0 (New UI + Single Video)

