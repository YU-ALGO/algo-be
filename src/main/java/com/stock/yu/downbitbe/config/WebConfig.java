package com.stock.yu.downbitbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(Config.WEB_BASE_URL)
                .allowedOrigins("http://localhost:3000")
//                .allowedOrigins("**") // for test API
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true);
    }
}