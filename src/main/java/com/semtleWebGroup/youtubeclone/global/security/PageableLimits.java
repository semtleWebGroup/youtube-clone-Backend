package com.semtleWebGroup.youtubeclone.global.security;

import java.lang.annotation.*;

/**
 * package :  com.semtleWebGroup.youtubeclone.global.security
 * fileName : PageableLimits
 * author :  ShinYeaChan
 * date : 2023-04-07
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)	// 런타임 시점까지 메모리 할당
@Target(ElementType.PARAMETER)	// 어노테이션 선언 가능 타입
public @interface PageableLimits {
    int maxSize() default 10;	// 페이지 조회 최대 개수
    int minSize() default 1;	// 페이지 조회 최소 개수
}