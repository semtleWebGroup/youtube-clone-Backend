package com.semtleWebGroup.youtubeclone.global.config;

import com.semtleWebGroup.youtubeclone.global.security.PageableLimits;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * package :  com.semtleWebGroup.youtubeclone.global.security.config
 * fileName : WebMvcConfig
 * author :  ShinYeaChan
 * date : 2023-04-07
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        int maxSize = Integer.MAX_VALUE;
        
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver() {
            @Override
            public Pageable resolveArgument(MethodParameter methodParameter, @Nullable ModelAndViewContainer mavContainer,
                                            NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {
                Pageable p = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
                return getLimitsFromAnnotation(p, methodParameter);
            }
            
            private Pageable getLimitsFromAnnotation(Pageable p, MethodParameter methodParameter) {
                
                PageableLimits limits = methodParameter.getParameterAnnotation(PageableLimits.class);
                
                if (limits == null) return p;
                
                if (p.getPageSize() > limits.maxSize())
                    return PageRequest.of(p.getPageNumber(), limits.maxSize(), p.getSort());
                else if (p.getPageSize() < limits.minSize())
                    return PageRequest.of(p.getPageNumber(), limits.minSize(), p.getSort());
                
                return p;
            }
        };
        resolver.setMaxPageSize(Integer.MAX_VALUE);
        resolvers.add(resolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}