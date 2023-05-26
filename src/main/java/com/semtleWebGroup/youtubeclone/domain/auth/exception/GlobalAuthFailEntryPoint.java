package com.semtleWebGroup.youtubeclone.domain.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class GlobalAuthFailEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("인증 실패");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //TODO : 인증 실패 예외를 세분화 하여 처리
    }
}
