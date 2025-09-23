package com.slow.care.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadBase = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
        // 절대경로 + 마지막에 슬래시 꼭!
        String location = "file:" + uploadBase.toAbsolutePath().toString() + "/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations(location)
                .setCachePeriod(0); // 개발 중 캐시 끄기
    }
}

