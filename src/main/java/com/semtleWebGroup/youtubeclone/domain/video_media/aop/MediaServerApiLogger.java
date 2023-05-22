package com.semtleWebGroup.youtubeclone.domain.video_media.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@Aspect
@Configuration
@EnableAspectJAutoProxy
public class MediaServerApiLogger {


    //pointcut : video_media.service 패키지 안에 있으면서, MediaServerSpokesman 인터페이스를 구현한 클래스의 모든 메소드에 적용
    @Pointcut("execution(* com.semtleWebGroup.youtubeclone.domain.video_media.service.*.*(..)) && target(com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman)")
    private void publicApiRequest() {
    }

    @Around("publicApiRequest()")
    public Object doLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.debug("AOP LOG :: MediaServerSpokesman method called: {}", proceedingJoinPoint.getSignature().toLongString());
        Object proceed = proceedingJoinPoint.proceed();
        log.debug("AOP LOG ::MediaServerSpokesman method finished: {}", proceedingJoinPoint.getSignature().toLongString());
        return proceed;
    }

    @AfterThrowing(pointcut = "publicApiRequest()", throwing = "e")
    public void doLogAfterThrowing(JoinPoint joinPoint, Throwable e) throws Throwable {
        log.error("AOP LOG ::MediaServerSpokesman method failed: {}", joinPoint.getSignature().toLongString(), e);
        throw e;
    }



}
