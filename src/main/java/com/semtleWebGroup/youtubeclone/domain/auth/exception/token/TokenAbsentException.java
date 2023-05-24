package com.semtleWebGroup.youtubeclone.domain.auth.exception.token;

import org.springframework.security.core.AuthenticationException;

public class TokenAbsentException extends AuthenticationException {
    public TokenAbsentException(String msg) {
        super(msg);
    }
}
