package com.semtleWebGroup.youtubeclone.domain.member.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * package :  com.semtleWebGroup.youtubeclone.global.error.exception.handler
 * fileName : MemberAccessDeniedHandler
 * author :  ShinYeaChan
 * date : 2023-05-05
 */
@Slf4j
public class MemberAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //TODO: access denied 됐을때 리다이렉트 경로 수정
        log.debug("리다이랙트");
        response.sendRedirect("/");
    }
}
