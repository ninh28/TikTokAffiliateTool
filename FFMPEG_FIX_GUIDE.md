# 🔧 SỬA LỖI FFMPEG - DRAWTEXT EXPRESSION

## ✅ Đã sửa các lỗi:

### 1. **FFmpeg Drawtext Expression Error**:
- ❌ **Lỗi cũ**: `enable='between(t\\\\,1\\\\,10)'` (quá nhiều backslash)
- ✅ **Đã sửa**: `enable='between(t,1,10)'` (bỏ escape thừa)

### 2. **Missing Music Files**:
- ❌ **Lỗi cũ**: Không tìm thấy file nhạc → FFmpeg lỗi
- ✅ **Đã sửa**: Tạo placeholder files + logic kiểm tra file size > 100 bytes

### 3. **Zoompan Expression Error**:
- ❌ **Lỗi cũ**: `if(eq(on\\\\,1)\\\\,1.5\\\\,max(zoom-0.0015\\\\,1.0))`
- ✅ **Đã sửa**: `if(eq(on,1),1.5,max(zoom-0.0015,1.0))`

## 🚀 Test lại:

### 1. Compile và chạy:
```bash
cd c:\Users\admin\IdeaProjects\TikTokAffiliateTool
mvn clean compile
mvn spring-boot:run
```

### 2. Test tạo video:
1. Truy cập http://localhost:8080
2. Upload ảnh
3. Nhấn "BẮT ĐẦU TẠO 3 VIDEO"
4. Kiểm tra logs - không còn lỗi FFmpeg -22

## 🎵 Về file nhạc:

### Hiện tại:
- ✅ Đã tạo placeholder files để tránh lỗi "file not found"
- ✅ Logic kiểm tra file size > 100 bytes (placeholder chỉ có vài chục bytes)
- ✅ Video sẽ được tạo không có nhạc nền (silent audio)

### Để có nhạc thật:
1. **Thay thế placeholder files** bằng file MP3 thật:
   - `src/main/resources/static/luxury_bgm.mp3` (12 giây)
   - `src/main/resources/static/chill_vibes.mp3` (12 giây)
   - `src/main/resources/static/fast_dynamic.mp3` (7 giây)

2. **Hoặc tạo silent audio bằng FFmpeg**:
```bash
# Nếu có FFmpeg trong PATH
ffmpeg -f lavfi -i anullsrc=r=44100:cl=stereo -t 12 -c:a aac -b:a 128k luxury_bgm.mp3
ffmpeg -f lavfi -i anullsrc=r=44100:cl=stereo -t 12 -c:a aac -b:a 128k chill_vibes.mp3  
ffmpeg -f lavfi -i anullsrc=r=44100:cl=stereo -t 7 -c:a aac -b:a 128k fast_dynamic.mp3
```

## 🎯 Kết quả mong đợi:

### Logs thành công:
```
🎯 TỰ ĐỘNG GÁN PROMPT: Đã gán 3 kịch bản ngầm
🚀 ZERO-INPUT WORKFLOW: Bắt đầu tạo 3 video tự động...
⚡ AUTO-FALLBACK: Khởi động 3 luồng song song
🎨 [Expert Review] Tạo video tĩnh + tự động hậu kỳ...
⚠️ [Video 1] Không tìm thấy nhạc hợp lệ (sẽ tạo video không nhạc)
⏱️ [Video 1] Tự động thời lượng: 12s
✅ [Expert Review] Tự động hậu kỳ hoàn thành: /videos/auto_1_xxx.mp4
```

### Không còn lỗi:
- ❌ `Missing ')' or too many args in 'between(t\,1\,10)'`
- ❌ `FFmpeg exit code -22`
- ❌ `Error opening output files: Invalid argument`

## 🔍 Nếu vẫn có lỗi:

1. **Kiểm tra FFmpeg path**: `C:\ffmpeg\ffmpeg.exe`
2. **Kiểm tra output directory**: `src/main/resources/static/videos/`
3. **Xem logs chi tiết** để debug
4. **Test với ảnh khác** (định dạng PNG/JPG)

Bây giờ FFmpeg sẽ hoạt động bình thường và tạo video thành công!