package com.semtleWebGroup.youtubeclone.domain.video_media.service;

import com.semtleWebGroup.youtubeclone.global.error.FieldError;
import com.semtleWebGroup.youtubeclone.domain.video_media.dto.response.GetEncodingStatusResponse;
import com.semtleWebGroup.youtubeclone.global.error.exception.MediaServerException;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MediaServerSpokesman {
    /**
     * 미디어 서버와 통신을 중계해 주는 api
     *
     * @param videoFile     비디오 파일
     * @param videoId       비디오 아이디
     * @param thumbnailFile 썸네일 파일 (없으면 null)
     * @return
     * @throws MediaServerException 미디어 서버의 응답이 200이 아닐 경우
     * @implNote 비디오 파일과 비디오 아이디는 필수 인자이고, 썸네일 파일은 없을 시 미디어 서버에서 첫 프레임을 썸네일로 지정함 <br>
     * 이 메서드는 비동기 적으로 처리되며, ok 사인은 인코딩이 완료 됨을 의미하지 않고, 쓰레드 풀의 대기열에 잘 등록됬다는 것을 의미함.
     */
    double sendEncodingRequest(MultipartFile videoFile, UUID videoId, @Nullable MultipartFile thumbnailFile) throws MediaServerException;

    /**
     * 인코딩 큐에 등록된 비디오의 인코딩 상태를 가져옴
     *
     * @param videoId 비디오 아이디
     * @return 인코딩 상태
     * @throws MediaServerException 미디어 서버의 응답 중 문제가 생겼을 경우
     */
    GetEncodingStatusResponse.EncodingStatus getEncodingStatus(UUID videoId) throws MediaServerException;

    /**
     * 비디오 삭제를 요청함
     *
     * @param videoId 비디오 아이디
     * @throws MediaServerException 미디어 서버의 응답 중 문제가 생겼을 경우
     */
    void deleteVideo(UUID videoId) throws MediaServerException;

    /**
     * 썸네일을 업데이트 함
     *
     * @param videoId
     * @param thumbnailFile
     * @throws MediaServerException
     */
    void patchThumbnail(UUID videoId, MultipartFile thumbnailFile) throws MediaServerException;

}
