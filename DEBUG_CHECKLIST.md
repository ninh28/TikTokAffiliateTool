# 🔧 KIỂM TRA CẤU HÌNH - DEBUG WHITELABEL ERROR

## Các bước kiểm tra lỗi:

### 1. Kiểm tra Console Logs:
Mở terminal và chạy ứng dụng, xem logs chi tiết:
```bash
cd c:\Users\admin\IdeaProjects\TikTokAffiliateTool
mvn spring-boot:run
```

### 2. Kiểm tra các file cần thiết:
- ✅ `VideoScenario.java` đã được tạo trong model package
- ✅ Import statements đã được cập nhật
- ✅ Method signatures đã được sửa
- ✅ Global error handler đã được thêm

### 3. Kiểm tra dependencies trong pom.xml:
Đảm bảo có các dependency cần thiết:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

### 4. Kiểm tra application.properties:
```properties
# Logging level để debug
logging.level.com.ninhquachhai.tiktoktool=DEBUG
logging.level.org.springframework.web=DEBUG

# Server config
server.port=8080
server.error.whitelabel.enabled=false
```

### 5. Các lỗi có thể gặp:

#### A. Compilation Error:
- ❌ VideoScenario class conflict → ✅ Đã sửa
- ❌ Import missing → ✅ Đã thêm
- ❌ Method signature mismatch → ✅ Đã sửa

#### B. Runtime Error:
- ❌ FFmpeg path không tồn tại
- ❌ Thư mục temp không tạo được
- ❌ File upload lỗi

### 6. Test cơ bản:
1. Truy cập http://localhost:8080
2. Upload 1 ảnh bất kỳ
3. Nhấn "BẮT ĐẦU TẠO 3 VIDEO"
4. Xem console logs để debug

### 7. Nếu vẫn lỗi Whitelabel:
Thêm vào application.properties:
```properties
server.error.whitelabel.enabled=false
server.error.path=/error
logging.level.web=DEBUG
```

### 8. Debug steps:
1. Kiểm tra logs trong console
2. Xem stack trace chi tiết
3. Kiểm tra file paths (FFmpeg, temp directories)
4. Test từng component riêng lẻ