package com.semtleWebGroup.youtubeclone.global.error.exception;

import com.semtleWebGroup.youtubeclone.domain.video_media.exception.VideoFileNotExistException;
import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;

/**
 * 로컬 파일과 관련된 예외의 부모 클래스
 *
 * @see VideoFileNotExistException
 */
public class LocalResourceException extends BusinessException{
    protected LocalResourceException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    protected LocalResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
