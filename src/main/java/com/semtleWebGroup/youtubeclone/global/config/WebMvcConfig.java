package com.semtleWebGroup.youtubeclone.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * package :  com.semtleWebGroup.youtubeclone.global.security.config
 * fileName : WebMvcConfig
 * author :  ShinYeaChan
 * date : 2023-04-07
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    private final long MAX_AGE_SECS =3600;
    
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET","POST","DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}