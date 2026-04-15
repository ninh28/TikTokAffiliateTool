package com.ninhquachhai.tiktoktool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name").toLowerCase();
        String videoLocation;

        if (os.contains("win")) {
            // Đường dẫn cho Windows khi chạy trong IDE
            videoLocation = "file:src/main/resources/static/videos/";
        } else {
            // Đường dẫn cho Linux (Render) - Thư mục /app/static/videos/ bên ngoài JAR
            videoLocation = "file:/app/static/videos/";
        }

        // Cấu hình để phục vụ file video từ đường dẫn tương ứng
        registry.addResourceHandler("/videos/**")
                .addResourceLocations(videoLocation);
        
        // Cấu hình cho nhạc nền nếu cần truy cập qua URL
        String musicLocation = os.contains("win") ? 
                "file:src/main/resources/static/music/" : "file:/app/static/music/";
        registry.addResourceHandler("/music/**")
                .addResourceLocations(musicLocation);

        // Serve static files mặc định từ classpath
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
