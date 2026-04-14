# 🚀 ZERO-INPUT WORKFLOW - HƯỚNG DẪN SỬ DỤNG

## Mục tiêu: "NGƯỜI DÙNG CHỈ CẦN 1 ẢNH"

Hệ thống đã được cập nhật để thực hiện quy trình hoàn toàn tự động:

### ⚡ QUY TRÌNH TỰ ĐỘNG:

1. **Upload 1 ảnh** → Nhấn "BẮT ĐẦU TẠO VIDEO"
2. **Hệ thống tự động**:
   - Sinh 3 kịch bản ngầm (Expert, Lifestyle, Trendy)
   - Gửi đồng thời 3 yêu cầu lên Luma AI
   - Anti-Stuck: Sau 3 phút tự chuyển FFmpeg
   - Tự động hậu kỳ: chèn nhạc + chữ
   - Trả về 3 video hoàn chỉnh

### 🎯 3 CHIẾN THUẬT TỰ ĐỘNG:

#### Video 1: Expert Review
- **Prompt**: Macro cận cảnh chất liệu
- **Nhạc**: luxury_bgm.mp3 (sang trọng)
- **Chữ**: "Hàng cao cấp soi kỹ chi tiết" (góc dưới)
- **Thời lượng**: 12 giây

#### Video 2: Lifestyle Vibe  
- **Prompt**: Đặt trong bối cảnh sang trọng
- **Nhạc**: chill_vibes.mp3 (nhẹ nhàng)
- **Chữ**: "Style cực xinh cho bạn" (chính giữa)
- **Thời lượng**: 12 giây

#### Video 3: Trendy Deal
- **Prompt**: Chuyển động nhanh, ánh sáng mạnh
- **Nhạc**: fast_dynamic.mp3 (sôi động)
- **Chữ**: "GIẢM 50% - MUA NGAY" (nhấp nháy)
- **Thời lượng**: 7 giây

### 🛡️ CƠ CHẾ ANTI-STUCK:

- **Luma AI**: Timeout 3 phút
- **Tự động chuyển**: FFmpeg với hiệu ứng zoom/pan
- **Đảm bảo**: Luôn có 3 video sau tối đa 4 phút

### 📱 TRẠNG THÁI THÔNG MINH:

1. "Đang gửi ảnh..."
2. "AI đang vẽ video..."
3. "AI quá lâu, đang chuyển sang tạo nhanh..."
4. "Đang lồng nhạc và chữ..."
5. "Hoàn tất!"

### 🎵 YÊU CẦU NHẠC NỀN:

Thêm 3 file nhạc vào `src/main/resources/static/`:
- `luxury_bgm.mp3` (12-15s)
- `chill_vibes.mp3` (12-15s)  
- `fast_dynamic.mp3` (7-10s)

### 🔧 CẤU HÌNH:

```properties
# Luma AI API (tùy chọn)
luma.api.key=your_luma_api_key_here

# FFmpeg Path
ffmpeg.path=C:/ffmpeg/ffmpeg.exe

# Font Path (Windows)
font.path=C:/Windows/Fonts/arial.ttf
```

### 🎉 KẾT QUẢ:

**Input**: 1 ảnh sản phẩm  
**Output**: 3 video Affiliate hoàn chỉnh (có AI/hiệu ứng + nhạc + chữ)  
**Thời gian**: Tối đa 4 phút  
**Tự động**: 100%