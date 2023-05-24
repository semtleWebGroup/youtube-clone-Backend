package com.semtleWebGroup.youtubeclone.domain.auth.exception.common;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.exception.BusinessException;

public class OccupiedEmailException extends BusinessException {
    public OccupiedEmailException(String message) {
        super(message, ErrorCode.OCCUPIED_EMAIL);
    }
}
