package com.semtleWebGroup.youtubeclone.domain.video.exception;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.exception.BusinessException;

public class VideoInfoExistException extends BusinessException {

    public VideoInfoExistException(String message) {
        super(ErrorCode.VIDEO_INFO_ALREADY_EXIST);
    }
}
