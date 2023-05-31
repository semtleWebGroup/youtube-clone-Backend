package com.semtleWebGroup.youtubeclone.domain.video.exception;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.exception.BusinessException;

public class MediaServerRequestException extends BusinessException {
    public MediaServerRequestException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
