package com.semtleWebGroup.youtubeclone.domain.video.exception;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.exception.BusinessException;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
