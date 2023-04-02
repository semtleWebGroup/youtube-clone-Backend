package com.semtleWebGroup.youtubeclone.global.error.exception;

import com.semtleWebGroup.youtubeclone.domain.video_media.exception.VideoFileNotExistException;
import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;

/**
 * BusinessException 의 직계 자손으로, 가져오려는 파일(OS FileSystem 의)이 존재하지 않음을 나타냄
 * 비디오파일, 리소스 파일 등을 가져와야 하는데 못가져오는 경우
 * 각각을 특정하기 위해 상속하여 사용하세요
 *
 * @see VideoFileNotExistException
 */
public class ResourceNotFoundException extends BusinessException{
    protected ResourceNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    protected ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
