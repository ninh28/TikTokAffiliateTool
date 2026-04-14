# 🚀 HƯỚNG DẪN CHẠY ỨNG DỤNG

## ✅ Đã sửa lỗi FFmpeg - Bây giờ chạy ứng dụng:

### 🎯 **Cách 1: Sử dụng IntelliJ IDEA (Khuyến nghị)**

1. **Mở IntelliJ IDEA**
2. **Open Project**: `C:\Users\admin\IdeaProjects\TikTokAffiliateTool`
3. **Chờ IntelliJ import dependencies** (Maven sẽ tự động download)
4. **Tìm file**: `TikTokAffiliateToolApplication.java`
5. **Click chuột phải** → **Run 'TikTokAffiliateToolApplication'**
6. **Hoặc nhấn**: `Ctrl + Shift + F10`

### 🎯 **Cách 2: Sử dụng Maven Wrapper**

```cmd
cd C:\Users\admin\IdeaProjects\TikTokAffiliateTool
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

### 🎯 **Cách 3: Chạy trực tiếp JAR**

```cmd
cd C:\Users\admin\IdeaProjects\TikTokAffiliateTool
java -jar target\TikTokAffiliateTool-0.0.1-SNAPSHOT.jar
```

## 🌐 **Test ứng dụng:**

1. **Truy cập**: http://localhost:8080
2. **Upload ảnh sản phẩm**
3. **Nhấn**: "BẮT ĐẦU TẠO 3 VIDEO"
4. **Chờ kết quả** (tối đa 4 phút)

## 📊 **Logs mong đợi:**

```
🎯 TỰ ĐỘNG GÁN PROMPT: Đã gán 3 kịch bản ngầm (Expert, Lifestyle, Trendy)
🚀 ZERO-INPUT WORKFLOW: Bắt đầu tạo 3 video tự động...
⚡ AUTO-FALLBACK: Khởi động 3 luồng song song (180s Luma → FFmpeg)...
🎨 [Expert Review] Tạo video tĩnh + tự động hậu kỳ...
⚠️ [Video 1] Không tìm thấy nhạc hợp lệ (sẽ tạo video không nhạc)
⏱️ [Video 1] Tự động thời lượng: 12s
✅ [Expert Review] Tự động hậu kỳ hoàn thành: /videos/auto_1_xxx.mp4
✅ [Lifestyle Vibe] Tự động hậu kỳ hoàn thành: /videos/auto_2_xxx.mp4
✅ [Trendy Deal] Tự động hậu kỳ hoàn thành: /videos/auto_3_xxx.mp4
🎉 AUTO-FALLBACK HOÀN TẤT: 3 video đã sẵn sàng!
```

## ❌ **Không còn lỗi:**

- ✅ FFmpeg drawtext expression đã sửa
- ✅ Placeholder music files đã tạo
- ✅ Thymeleaf binding đã fix
- ✅ VideoCount validation đã sửa

## 🎵 **Để có nhạc thật:**

Thay thế các file trong `src/main/resources/static/`:
- `luxury_bgm.mp3` (12 giây)
- `chill_vibes.mp3` (12 giây)  
- `fast_dynamic.mp3` (7 giây)

## 🔧 **Nếu vẫn có vấn đề:**

1. **Kiểm tra Java version**: `java -version` (cần Java 17+)
2. **Kiểm tra FFmpeg**: `C:\ffmpeg\ffmpeg.exe -version`
3. **Xem logs console** trong IntelliJ
4. **Kiểm tra port 8080** có bị chiếm không

**Khuyến nghị**: Sử dụng IntelliJ IDEA để chạy, nó sẽ tự động handle Maven dependencies!