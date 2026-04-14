# 🎬 Video Re-up Tool - Hướng Dẫn Sử Dụng

## ✨ Tính Năng Chính

Tool này giúp bạn **tái sử dụng video** bằng cách tự động tạo **3 biến thể khác nhau** từ một video gốc, sử dụng các kỹ thuật:

### 1. **Thay Đổi Tỷ Lệ Khung Hình**
- **Lật Hình (Mirror/Flip)** - Lật video theo chiều ngang
- **Cắt Hình (Crop)** - Thu hẹp hình ảnh để tập trung vào nội dung
- **Zoom** - Phóng to/thu nhỏ nhẹ

### 2. **Cắt Ghép Nội Dung**
- Thay đổi tốc độ video (speed up/slow down)
- Cắt bỏ các phần không cần thiết

### 3. **Thêm Hiệu Ứng & Bộ Lọc**
- Điều chỉnh độ sáng, độ tương phản, độ bão hòa màu
- Áp dụng các hiệu ứng màu sắc khác nhau (grayscale, sepia, vibrant, v.v.)

### 4. **Chèn Text & Watermark**
- Thêm phụ đề (subtitle) vào video
- Thêm watermark (logo, chế độ bảo vệ bản quyền)
- Chèn timestamp

---

## 🚀 Hướng Dẫn Sử Dụng

### Bước 1: Truy Cập Tool
1. Mở trình duyệt
2. Truy cập: `http://localhost:8080/reup`

### Bước 2: Tải Video Lên
1. Nhấp vào khu vực **"Kéo thả video hoặc Click để chọn"**
2. Hoặc kéo video trực tiếp vào khu vực
3. Chọn video từ máy tính (hỗ trợ MP4, MOV, AVI, MKV, WebM)

### Bước 3: Chọn Hiệu Ứng
Chọn các tùy chọn bạn muốn áp dụng:

| Tùy Chọn | Mô Tả | Mặc Định |
|---------|-------|---------|
| **Lật Hình** | Lật video theo chiều ngang | ❌ Tắt |
| **Cắt Hình** | Thu hẹp hình ảnh | ✅ Bật |
| **Tốc Độ Video** | Nhân với hệ số tốc độ (1.1 = nhanh 10%) | 1.1 |
| **Thêm Phụ Đề** | Chèn text vào video | ❌ Tắt |
| **Thêm Bộ Lọc** | Áp dụng hiệu ứng màu sắc | ❌ Tắt |
| **Thêm Watermark** | Chèn logo/text góc video | ❌ Tắt |

### Bước 4: Bắt Đầu Xử Lý
1. Nhấp nút **"Bắt Đầu Xử Lý"**
2. Chờ tool xử lý (thời gian phụ thuộc vào độ dài video)
3. Màn hình sẽ hiển thị thanh tiến độ

### Bước 5: Tải Xuống Kết Quả
1. Sau khi xử lý hoàn tất, bạn sẽ thấy **3 biến thể video**
2. Mỗi biến thể có một nút **"Tải Xuống"**
3. Nhấp để tải video về máy tính

---

## 📊 3 Biến Thể Video Được Tạo Ra

### **Biến Thể 1: Tỷ Lệ Khung Hình Focus** 🎯
- Áp dụng: Mirror (lật hình) + Crop (cắt hình)
- Độ sáng: +5% brightness
- Tốc độ: Theo cấu hình người dùng
- **Mục đích**: Tập trung vào nội dung từ góc độ khác

### **Biến Thể 2: Bộ Lọc & Hiệu Ứng Focus** 🌈
- Áp dụng: Hiệu ứng màu sắc vibrant (bão hòa +30%)
- Độ tương phản: +20%
- Mirror (nếu bật)
- **Mục đích**: Video sáng, rực rỡ, hấp dẫn

### **Biến Thể 3: Nội Dung & Text Focus** ⚡
- Áp dụng: Aggressive crop (cắt 85%)
- Nâng cấp tốc độ (x1.2 so với cài đặt gốc)
- Text overlay (nếu có)
- **Mục đích**: Video năng động, tập trung, kích thích hành động

---

## ⚙️ Cài Đặt & Yêu Cầu

### Yêu Cầu Hệ Thống
- **Java 17** trở lên
- **FFmpeg** cài đặt tại `C:\ffmpeg\ffmpeg.exe`
- **Spring Boot 4.0.5**
- Dung lượng đĩa: Ít nhất 5GB cho video tạm thời

### Cài Đặt FFmpeg (Windows)
1. Tải FFmpeg: https://ffmpeg.org/download.html
2. Cài đặt tại: `C:\ffmpeg\`
3. Kiểm tra: Mở cmd, gõ `C:\ffmpeg\ffmpeg.exe -version`

---

## 🎨 Ví Dụ Sử Dụng

### Ví Dụ 1: Video Sản Phẩm Affiliate
**Mục tiêu**: Tạo 3 biến thể từ 1 video sản phẩm để tải lên 3 TikTok khác nhau

**Cấu hình**:
- ✅ Mirror: Bật
- ✅ Crop: Bật
- Tốc độ: 1.1
- Phụ đề: "Sản phẩm chất lượng cao - Xem ngay"
- ✅ Watermark: "Link bio"

**Kết quả**: 3 video hoàn toàn khác nhau, không bị phát hiện là copy

### Ví Dụ 2: Video Hướng Dẫn
**Mục tiêu**: Re-up video hướng dẫn với các góc nhìn khác

**Cấu hình**:
- Mirror: Tắt
- ✅ Crop: Bật
- Tốc độ: 1.05 (nhẹ nhanh hơn)
- ✅ Filter: Bật (tăng độ sáng)
- Phụ đề: Từng bước hướng dẫn

**Kết quả**: 3 phiên bản cùng nội dung nhưng trình bày khác

---

## 🐛 Khắc Phục Sự Cố

### Lỗi: "FFmpeg not found"
**Giải pháp**: 
- Kiểm tra FFmpeg cài đặt tại `C:\ffmpeg\ffmpeg.exe`
- Hoặc chỉnh sửa đường dẫn trong file `ReupVariationEngine.java`

### Lỗi: "Upload failed"
**Giải pháp**:
- Kiểm tra dung lượng video (tối đa 500MB khuyến nghị)
- Kiểm tra định dạng video (hỗ trợ MP4, MOV, AVI, MKV)
- Xóa cache trình duyệt và tải lại

### Lỗi: "Processing timeout"
**Giải pháp**:
- Giảm độ dài video hoặc độ phân giải
- Tăng timeout trong Spring Boot `application.properties`:
  ```properties
  server.tomcat.connection-timeout=600000
  ```

### Lỗi: "Out of memory"
**Giải pháp**:
- Tăng bộ nhớ Java: `JAVA_OPTS=-Xmx4g`
- Hoặc xử lý video ngắn hơn

---

## 📈 Hiệu Năng & Tối Ưu Hóa

### Thời Gian Xử Lý
- Video 1 phút: ~5-10 giây
- Video 5 phút: ~25-50 giây
- Video 10 phút: ~50-100 giây

**Lưu ý**: Thời gian phụ thuộc vào độ phân giải và CPU máy tính

### Mẹo Tối Ưu
1. **Dùng video độ phân giải 1080p** (không cần 4K)
2. **Video dưới 5 phút** xử lý nhanh hơn
3. **Tắt bộ lọc phức tạp** nếu không cần thiết
4. **Sử dụng SSD** để lưu file tạm thời

---

## 📝 Lưu Ý Quan Trọng

⚠️ **Không dùng tool này để**:
- Copy-paste hoàn toàn video của người khác (vi phạm bản quyền)
- Tạo nội dung giả mạo hoặc lừa đảo
- Bypass hệ thống phát hiện trùng lặp với ý định gian lận

✅ **Dùng tool này để**:
- Tạo biến thể hợp pháp từ video của bạn
- Tối ưu hóa nội dung cho các platform khác nhau
- Thử nghiệm các hiệu ứng khác nhau

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề:
1. Kiểm tra file log trong console
2. Xem phần "Khắc Phục Sự Cố" ở trên
3. Kiểm tra kết nối internet (nếu cần tải resource)

---

**Version**: 1.0.0  
**Cập nhật lần cuối**: 2024  
**Ngôn ngữ**: Java Spring Boot + FFmpeg + HTML/CSS/JS

