package com.semtleWebGroup.youtubeclone.domain.video_media.service;

import com.semtleWebGroup.youtubeclone.global.error.exception.MediaServerException;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * 미디어 서버와 통신을 중계해 주는 api <br>
 * 추상화를 통해 제공 되는 메서드로 미디어 서버와 통신할 수 있게 함
 */
@Service
public class MediaServerSpokesman {

    public enum EncodingStatus{WAIT, RUNNING, FINISHED, ERROR};

    /**
     * 미디어 서버와 통신을 중계해 주는 api
     * @param videoFile 비디오 파일
     * @param videoId 비디오 아이디
     * @param thumbnailFile 썸네일 파일 (없으면 null)
     * @throws MediaServerException 미디어 서버의 응답이 200이 아닐 경우
     * @implNote 비디오 파일과 비디오 아이디는 필수 인자이고, 썸네일 파일은 없을 시 미디어 서버에서 첫 프레임을 썸네일로 지정함 <br>
     * 이 메서드는 비동기 적으로 처리되며, ok 사인은 인코딩이 완료 됨을 의미하지 않고, 쓰레드 풀의 대기열에 잘 등록됬다는 것을 의미함.
     */
    public void sendEncodingRequest(FilePart videoFile, UUID videoId, @Nullable FilePart thumbnailFile) throws MediaServerException {
        Assert.notNull(videoFile,"videoFile must not be null");
        Assert.notNull(videoId,"videoId must not be null");
    }

    /**
     * 인코딩 큐에 등록된 비디오의 인코딩 상태를 가져옴
     * @param videoId 비디오 아이디
     * @return 인코딩 상태
     * @throws MediaServerException 미디어 서버의 응답 중 문제가 생겼을 경우
     */
    public EncodingStatus getEncodingStatus(UUID videoId) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");
        return EncodingStatus.FINISHED;
    }

    /**
     * 비디오 삭제를 요청함
     * @param videoId 비디오 아이디
     * @throws MediaServerException 미디어 서버의 응답 중 문제가 생겼을 경우
     */
    public void deleteVideo(UUID videoId) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");
    }

    /**
     * 썸네일을 업데이트 함
     * @param videoId
     * @param thumbnailFile
     * @throws MediaServerException
     */
    public void patchThumbnail(UUID videoId, FilePart thumbnailFile) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");
        Assert.notNull(thumbnailFile,"thumbnailFile must not be null");
    }

}
