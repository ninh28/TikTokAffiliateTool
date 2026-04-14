# 🚀 QUICK START - Hướng Dẫn Nhanh v2.0

## ⚡ 5 Bước Để Bắt Đầu

### **Bước 1: Build & Run Project**
```powershell
# Vào thư mục project
cd C:\Users\admin\IdeaProjects\TikTokAffiliateTool

# Build
mvn clean package -DskipTests

# Run
java -jar target/TikTokAffiliateTool-*.jar
```

### **Bước 2: Mở Trình Duyệt**
```
Truy cập: http://localhost:8080/reup/modern
```

### **Bước 3: Upload Video**
- Kéo thả video vào vùng drop-zone
- Hoặc click để chọn file
- Hỗ trợ: MP4, MOV, AVI, MKV (Max 500MB)

### **Bước 4: Cấu Hình & Nhấn "BẮT ĐẦU RE-UP"**
- **Auto Mode** (mặc định): Bật → Cắt + Zoom + Tốc độ
- **Expert Mode**: Tắt Auto → Tùy chỉnh từng tùy chọn
- Nhập phụ đề (tùy chọn)
- Click **BẮT ĐẦU RE-UP**

### **Bước 5: Tải Video**
- Đợi progress bar 0→100% (2-5 phút)
- Click **Tải video** khi hoàn thành

---

## 📊 So Sánh Trước/Sau

### **Giao Diện**
| Tiêu Chí | Trước | Sau |
|---------|-------|-----|
| **Theme** | Light | Dark Mode |
| **Color** | Xanh dương | Tím-Xanh Neon |
| **Layout** | Phức tạp | 3 khối rõ ràng |
| **Cards** | Nhỏ gọn | Spacious |
| **Effects** | Tối thiểu | Rich animations |
| **Mobile** | Có nhưng không tối ưu | Full responsive |

### **Functionality**
| Tiêu Chí | Trước | Sau |
|---------|-------|-----|
| **Output** | 3 videos | ✅ **1 video** |
| **Processing** | Lâu | Nhanh hơn |
| **Progress** | Polling chậm | Real-time |
| **Download** | Phức tạp | Trực tiếp |
| **Error Msg** | Không rõ | Chi tiết + Alert |

### **Code**
| Tiêu Chí | Trước | Sau |
|---------|-------|-----|
| **Route** | `/reup/modern` → cũ | `/reup/modern` → mới |
| **Backup** | Không có | `/reup/modern-old` |
| **Endpoints** | `/api/status/` | `/api/status/` + `/api/job-status/` |
| **Errors** | Path conversion bug | ✅ Sửa rồi |

---

## 🎨 UI Features Highlight

### **Drop Zone**
```
┌─────────────────────────────┐
│   ☁️                        │
│   Kéo thả video vào đây    │
│   MP4, MOV, AVI, MKV       │
│   Max 500MB                │
└─────────────────────────────┘
```

### **Settings Block**
```
┌─────────────────────────────┐
│ ⚙️ Cấu Hình Re-up          │
│                             │
│ 🤖 Auto Re-up ────── [ON]  │
│                             │
│ ┌─────────────────────────┐ │
│ │ 🎬 Cắt xén & Zoom [✓] │ │
│ │ ⏩ Tăng tốc 1.1x [✓]  │ │
│ │ 🔄 Lật Mirror    [ ]  │ │
│ │ 🔇 Tắt âm thanh [ ]  │ │
│ └─────────────────────────┘ │
└─────────────────────────────┘
```

### **Content Block**
```
┌─────────────────────────────┐
│ 📝 Phụ đề (Tùy chọn)       │
│ ┌─────────────────────────┐ │
│ │ Nhập phụ đề video...   │ │
│ └─────────────────────────┘ │
│                             │
│ Progress: ████████░░ 80%   │
└─────────────────────────────┘
```

### **Button**
```
┌─────────────────────────────┐
│  ▶️  BẮT ĐẦU RE-UP         │
│  (Gradient tím→xanh)        │
└─────────────────────────────┘
```

---

## 💡 Tips & Tricks

### **Chế Độ Auto (Khuyên dùng)**
✅ Tự động cắt, zoom, tốc độ  
✅ Tránh nhầm lẫn cấu hình  
✅ **Khuyên dùng cho 99% người dùng**

### **Chế Độ Expert**
- Muốn **lật video**: Bật Mirror
- Muốn **loại bỏ âm gốc**: Bật "Tắt âm thanh"
- Muốn **tùy chỉnh từng thứ**: Dùng chế độ này

### **Phụ Đề**
- Thêm tiêu đề khiến video **nổi bật hơn**
- Tự động hiển thị ở 3 giây đầu
- Giúp tăng **engagement trên TikTok**

### **Video Quality**
- Video nhỏ < 100MB: Xử lý nhanh ✅
- Video lớn 100-500MB: Chịu khó chờ ⏳
- **Khuyên dùng**: 50-150MB cho tốc độ tối ưu

---

## 🔧 Troubleshooting

### **❌ Lỗi: "Giao diện không hiện"**
```
✅ Giải pháp:
1. Xóa cache trình duyệt: Ctrl+Shift+Delete
2. Refresh trang: Ctrl+R
3. Thử truy cập: http://localhost:8080/reup/modern-old
4. Check console: F12 → Console tab
```

### **❌ Lỗi: "File quá lớn"**
```
✅ Giải pháp:
- Max: 500MB (set trong application.properties)
- Compress video: Dùng HandBrake hoặc FFmpeg
- Cắt video thành từng phần nhỏ
```

### **❌ Lỗi: "Upload thất bại"**
```
✅ Giải pháp:
1. Check định dạng: MP4, MOV, AVI, MKV
2. Thử file khác
3. Check internet connection
4. Restart application
```

### **❌ Lỗi: "Xử lý quá lâu"**
```
✅ Giải pháp:
- Video quá dài → Cắt lại
- FFmpeg chậm → Upgrade FFmpeg
- Max timeout: 10 phút
- Nếu quá 10 phút → Refresh & upload lại
```

### **❌ Lỗi: "Không tải được video"**
```
✅ Giải pháp:
1. Check xem job đã COMPLETED chưa
2. Xóa /temp_uploads/ rồi upload lại
3. Check disk space
4. Restart application
```

---

## 📞 Kiểm Tra Hệ Thống

### **Kiểm Tra FFmpeg**
```powershell
# Windows PowerShell
where ffmpeg
where ffprobe

# Output nên là đường dẫn FFmpeg
# Nếu không tìm thấy → Cài đặt FFmpeg
```

### **Kiểm Tra Application**
```powershell
# Check server chạy
curl http://localhost:8080

# Check endpoint
curl http://localhost:8080/reup/modern

# Check API
curl http://localhost:8080/reup/api/status/test-job
```

### **Kiểm Tra Logs**
```powershell
# Run với verbose mode
mvn clean compile -X

# Check target folder
ls target/

# Run và xem logs
java -jar target/TikTokAffiliateTool-*.jar
```

---

## 📁 Cấu Trúc Thư Mục

```
TikTokAffiliateTool/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ninhquachhai/tiktoktool/
│   │   │       ├── controller/
│   │   │       │   ├── ModernReupController.java  ← 🔄 SỬA
│   │   │       │   └── ...
│   │   │       ├── service/
│   │   │       │   ├── SingleVideoReupService.java
│   │   │       │   └── ...
│   │   │       └── model/
│   │   │           └── VideoReupJob.java
│   │   │
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── reup_new.html             ← 🆕 UI MỚI
│   │       │   ├── reup_modern_clean.html   ← UI cũ (backup)
│   │       │   └── ...
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   ├── videos/reup/             ← Output videos 📹
│   │       │   └── ...
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│
├── target/
│   ├── classes/                    ← Compiled
│   └── TikTokAffiliateTool-*.jar  ← Runnable JAR
│
├── REUP_NEW_UI_GUIDE.md           ← 📖 Hướng dẫn chi tiết
├── REUP_CHANGES.md                ← 📝 Summary thay đổi
├── REUP_QUICKSTART_v2.md          ← 📖 File này (Quick Start v2)
├── pom.xml
└── mvnw (Windows)
```

---

## 🎯 Next Steps

### **Ngay Bây Giờ**
1. [x] Build project
2. [x] Run application
3. [ ] Truy cập: `http://localhost:8080/reup/modern`
4. [ ] Upload video test
5. [ ] Kiểm tra output (1 video duy nhất)

### **Sau Đó**
- [ ] Tùy chỉnh CSS (nếu cần)
- [ ] Thêm more features
- [ ] Deploy to production
- [ ] Monitor performance

---

## 📚 Tài Liệu Thêm

| File | Nội Dung |
|------|---------|
| `REUP_NEW_UI_GUIDE.md` | Hướng dẫn chi tiết, API endpoints, troubleshooting |
| `REUP_CHANGES.md` | Tóm tắt thay đổi, file structure, testing checklist |
| `REUP_QUICKSTART_v2.md` | **File này** - Quick start nhanh |

---

## ✅ Checklist Để Bắt Đầu

- [ ] Cài đặt FFmpeg (nếu chưa có)
- [ ] Java 11+ đã cài
- [ ] Maven đã cài
- [ ] CD vào thư mục project
- [ ] Run `mvn clean package -DskipTests`
- [ ] Run `java -jar target/TikTokAffiliateTool-*.jar`
- [ ] Truy cập `http://localhost:8080/reup/modern`
- [ ] Upload video test
- [ ] Kiểm tra kết quả ✅

---

**Status**: ✅ READY TO USE  
**Ngày**: 2026-04-14  
**Version**: 2.0 Quick Start v2

