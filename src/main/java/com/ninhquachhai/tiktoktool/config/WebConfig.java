package com.ninhquachhai.tiktoktool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve video files từ thư mục videos
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:src/main/resources/static/videos/");
        
        // Serve static files mặc định
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}