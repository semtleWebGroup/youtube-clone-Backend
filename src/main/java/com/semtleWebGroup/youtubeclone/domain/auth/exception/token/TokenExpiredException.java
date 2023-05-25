package com.semtleWebGroup.youtubeclone.domain.auth.exception.token;

import org.springframework.security.core.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {
    public TokenExpiredException(String msg) {
        super(msg);
    }
}
