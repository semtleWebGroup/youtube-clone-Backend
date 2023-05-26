package com.semtleWebGroup.youtubeclone.domain.auth.filter;

import com.semtleWebGroup.youtubeclone.domain.auth.dto.CustomAuthentication;
import com.semtleWebGroup.youtubeclone.domain.auth.exception.token.TokenAbsentException;
import com.semtleWebGroup.youtubeclone.domain.auth.exception.token.TokenExpiredException;
import com.semtleWebGroup.youtubeclone.domain.auth.token.AccessToken;
import com.semtleWebGroup.youtubeclone.domain.auth.token.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 모든 인증이 필요한 요청을 가로막는 필터
 */
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final AccessToken.TokenBuilder accessTokenBuilder;
    public JwtAuthorizationFilter(AccessToken.TokenBuilder accessTokenBuilder) {
        this.accessTokenBuilder = accessTokenBuilder;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1. check request is contain token
        AccessToken accessToken;
        try {
            accessToken = this.resolveToken(request);
        } catch (TokenAbsentException e) {
            filterChain.doFilter(request, response);
            return;
        }

        //2. check token

        Token.TokenStatus tokenStatus = accessToken.validateToken();
        if (tokenStatus.equals(Token.TokenStatus.VALID)) {
            log.debug("The token passed to the request that requires authentication is valid ( {} ).",request.getRequestURI());
        } else if (tokenStatus.equals(Token.TokenStatus.INVALID)){
            log.debug("The token passed to the request that requires authentication is invalid ( {} ).",request.getRequestURI());
            throw new BadCredentialsException("token is invalid");
        } else if (tokenStatus.equals(Token.TokenStatus.EXPIRED)) {
            throw new TokenExpiredException("token is expired");
        }

        //3. parse token
        Map<AccessToken.Field, String> map = accessToken.parseClaims();

        //4. check channelId
        boolean channelIdExist = StringUtils.hasText(map.get(AccessToken.Field.CHANNEL_ID));
        if (!channelIdExist) {
            // 4-1. if channelId is not exist, check request is /channel and POST
            if (request.getRequestURI().equals("/channels") && request.getMethod().equals("POST")) {
                CustomAuthentication customAuthentication = new CustomAuthentication(map);
                SecurityContextHolder.getContext().setAuthentication(customAuthentication);
            }
            // 동작 설명 -> 채널 회원가입 로직이면, 통과, 아니면 401
            filterChain.doFilter(request, response);
            return;
        } else {
            // 4-2. if channelId is exist, set authentication
            CustomAuthentication customAuthentication = new CustomAuthentication(map);
            SecurityContextHolder.getContext().setAuthentication(customAuthentication);
            filterChain.doFilter(request, response);
        }

    }


    protected AccessToken resolveToken(HttpServletRequest request) throws TokenAbsentException {

        boolean hasToken = false;

        // 1. extract token from header
        String authorization = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            hasToken = true;
        }

        // 2. check
        if (!hasToken || !StringUtils.hasText(token)) {
            throw new TokenAbsentException("token is empty");
        }


        // 3. made it AccessToken and return
        return accessTokenBuilder.build(token);
    }
}
