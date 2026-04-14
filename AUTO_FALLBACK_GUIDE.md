# 🚀 AUTO-FALLBACK SYSTEM - HƯỚNG DẪN HOÀN CHỈNH

## ✨ MỤC TIÊU: TỰ ĐỘNG HÓA 100%

Hệ thống đã được cập nhật để loại bỏ hoàn toàn thao tác thủ công:

### 🎯 QUY TRÌNH AUTO-FALLBACK:

1. **Upload 1 ảnh** → Nhấn "BẮT ĐẦU TẠO 3 VIDEO"
2. **Tự động gán Prompt ngầm**: Nếu 3 ô Prompt trống → Hệ thống tự điền
3. **Auto-Fallback Timer**: 180 giây (3 phút) Luma AI → Tự động chuyển FFmpeg
4. **Hậu kỳ tự động**: Lồng nhạc + chèn chữ theo 3 phong cách
5. **Kết quả**: 3 video hoàn chỉnh trong tối đa 4 phút

### 🔄 CƠ CHẾ AUTO-FALLBACK:

#### Thay vì nút "Hủy" thủ công:
- ✅ **Bộ đếm ngược tự động**: 180 giây
- ✅ **Tự động ngắt kết nối**: Không cần can thiệp
- ✅ **Chuyển đổi mượt mà**: Luma AI → FFmpeg
- ✅ **Đảm bảo kết quả**: Luôn có video sau 4 phút

#### Trạng thái thông minh:
```
"Đang chờ AI vẽ (Tự động chuyển sang tạo nhanh sau 3 phút nếu AI quá tải)..."
```

### 🎨 3 KỊCH BẢN TỰ ĐỘNG:

#### 1. Expert Review
- **Prompt**: "Cinematic macro shot of product texture..."
- **Chữ**: "Hàng cao cấp soi kỹ chi tiết" (góc dưới)
- **Nhạc**: luxury_bgm.mp3
- **Thời lượng**: 12 giây

#### 2. Lifestyle Vibe  
- **Prompt**: "Product in elegant lifestyle setting..."
- **Chữ**: "Style cực xinh cho bạn" (chính giữa)
- **Nhạc**: chill_vibes.mp3
- **Thời lượng**: 12 giây

#### 3. Trendy Deal
- **Prompt**: "Fast dynamic zoom in and zoom out..."
- **Chữ**: "GIẢM 50% - MUA NGAY" (nhấp nháy)
- **Nhạc**: fast_dynamic.mp3
- **Thời lượng**: 7 giây

### 🛡️ TÍNH NĂNG BẢO VỆ:

#### Auto-Fallback Protection:
- **Luma AI**: 180s timeout → Tự động hủy
- **FFmpeg Backup**: Hiệu ứng zoom/pan tự động
- **Total Timeout**: 240s (4 phút) tối đa
- **Error Handling**: Thông báo lỗi cụ thể (401/402)

#### Zero-Input Protection:
- **Tên sản phẩm trống**: Tự động đặt "Sản phẩm"
- **Mô tả trống**: Tự động đặt "Mô tả sản phẩm"  
- **Prompt trống**: Tự động gán 3 kịch bản cố định

### 📱 GIAO DIỆN MỚI:

#### Loại bỏ:
- ❌ Nút "Hủy Luma AI" thủ công
- ❌ Chờ đợi người dùng can thiệp
- ❌ Nhập prompt bắt buộc

#### Thêm mới:
- ✅ Nút "BẮT ĐẦU TẠO 3 VIDEO" duy nhất
- ✅ Progress bar tự động
- ✅ Countdown timer 180s
- ✅ Status updates thông minh

### 🎵 YÊU CẦU NHẠC NỀN:

Thêm 3 file nhạc vào `src/main/resources/static/`:
```
luxury_bgm.mp3      (12-15s, sang trọng)
chill_vibes.mp3     (12-15s, nhẹ nhàng)  
fast_dynamic.mp3    (7-10s, sôi động)
```

### 🔧 CẤU HÌNH:

```properties
# Luma AI API (tùy chọn - có thể để trống)
luma.api.key=your_luma_api_key_here

# FFmpeg Path (bắt buộc)
ffmpeg.path=C:/ffmpeg/ffmpeg.exe

# Font Path (Windows)
font.path=C:/Windows/Fonts/arial.ttf

# Auto-Fallback Settings
auto.fallback.timeout=180
auto.fallback.enabled=true
```

### 🎉 KẾT QUẢ CUỐI CÙNG:

**Input**: 1 ảnh sản phẩm  
**Process**: 100% tự động (không cần thao tác)  
**Output**: 3 video Affiliate hoàn chỉnh  
**Time**: Tối đa 4 phút  
**Reliability**: Auto-Fallback đảm bảo luôn có kết quả

### 🚨 XỬ LÝ LỖI THÔNG MINH:

- **401 (Sai API Key)**: "Lỗi: Sai API Key Luma"
- **402 (Hết Credit)**: "Lỗi: Hết lượt tạo Video AI"  
- **Timeout**: "Auto-Fallback: Chuyển sang tạo nhanh"
- **Network Error**: Retry 3 lần → Fallback FFmpeg

### 📊 MONITORING:

```
🎯 Tiến độ: 1/3 video hoàn thành
📱 [Expert Review] Đang chờ AI vẽ (Tự động chuyển sang tạo nhanh sau 165s nếu AI quá tải)...
⏰ [Lifestyle Vibe] Auto-Fallback kích hoạt: Hủy Luma sau 180s
✅ [Trendy Deal] Auto-Fallback FFmpeg thành công!
```

**Kết luận**: Hệ thống hoàn toàn tự động, không cần can thiệp thủ công, đảm bảo luôn có kết quả!