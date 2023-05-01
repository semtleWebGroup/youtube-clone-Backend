package com.semtleWebGroup.youtubeclone.domain.video.exception;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.exception.BusinessException;

public class VideoNotCachedException extends BusinessException {
    public VideoNotCachedException(String message) {
        super(ErrorCode.VIDEO_NOT_CACHED);
    }
}
