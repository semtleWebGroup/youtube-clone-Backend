package com.semtleWebGroup.youtubeclone.global.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] excludePath = {"/members", "/members/session", "/home"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = jwtTokenProvider.parseBearerToken(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                expectValid(token);
            }
        } catch (TokenExpiredException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired");
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
        } catch (RuntimeException e) {
            logger.error("Could not set user authentication in security context", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error");
        } finally {
            filterChain.doFilter(request, response);
        }
    }
    
    
    private void expectValid(String token) {
        if (!jwtTokenProvider.validateToken(token) || token == null || token.isEmpty()) {
            throw new JWTVerificationException("Token is invalid.");
        }
        SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getMemberAuthenticationByToken(token));
    }
    
}
