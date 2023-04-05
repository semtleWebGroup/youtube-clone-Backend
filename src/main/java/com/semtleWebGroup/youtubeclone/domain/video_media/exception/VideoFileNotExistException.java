package com.semtleWebGroup.youtubeclone.domain.video_media.exception;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.exception.LocalResourceException;
import org.springframework.core.io.Resource;

public class VideoFileNotExistException extends LocalResourceException {

    private final Resource reasonResource;

    /**
     * 비디오 파일이 존재하지 않음을 의미하는 예외
     * 이는 단순히 요청한 비디오 파일이 없음을 의미하는 것이 아닌, 있어야하는데 없는 경우를 의미함
     *
     * @param reasonResource : 존재하지 않는 비디오 리소스
     */
    public VideoFileNotExistException(Resource reasonResource) {
        super("Video File Not Exist", ErrorCode.VIDEO_NOT_EXIST);
        this.reasonResource = reasonResource;
    }
}
