package com.semtleWebGroup.youtubeclone.domain.video_media.service;

import com.semtleWebGroup.youtubeclone.domain.video_media.dto.request.ThumbnailPatchRequest;
import com.semtleWebGroup.youtubeclone.domain.video_media.dto.request.VodInitPostRequest;
import com.semtleWebGroup.youtubeclone.domain.video_media.dto.response.GetEncodingStatusResponse;
import com.semtleWebGroup.youtubeclone.domain.video_media.dto.response.VodInitPostResponse;
import com.semtleWebGroup.youtubeclone.global.error.exception.MediaServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 운영 배포환경 전용 미디어 서버 중계 서비스
 */
@Slf4j
public class ProdMediaServerSpokesman implements MediaServerSpokesman {

    private final String mediaServerUrl;

    private final RestTemplate restTemplate;

    public ProdMediaServerSpokesman(String mediaServerUrl) {
        this.mediaServerUrl = mediaServerUrl;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public double sendEncodingRequest(MultipartFile videoFile, UUID videoId, @Nullable MultipartFile thumbnailFile) throws MediaServerException {
        Assert.notNull(videoFile, "videoFile must not be null");
        Assert.notNull(videoId, "videoId must not be null");

        //요소 정의
        String url = String.format("%s/media/vods/%s", mediaServerUrl, videoId);
        log.debug("send encoding request to media server. url: {}", url);

        VodInitPostRequest httpRequest = VodInitPostRequest.of(videoFile, thumbnailFile);

        //미디어 서버와 통신
        ResponseEntity<VodInitPostResponse> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(httpRequest), VodInitPostResponse.class);
        log.debug("response from media server: {}", response);

        //응답 확인
        HttpStatus statusCode = response.getStatusCode();

        if (response.getBody() == null) {
            //To-Do 예외 처리
            throw new MediaServerException();
        } else if (statusCode == HttpStatus.OK) {
            return response.getBody().getVideoLength();
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            //To-Do 예외처리
            throw new MediaServerException();
        } else {
            //To-Do 예외처리
            throw new MediaServerException();
        }
    }

    @Override
    public GetEncodingStatusResponse.EncodingStatus getEncodingStatus(UUID videoId) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");

        String url = String.format("%s/media/vods/%s/encoding/status", mediaServerUrl, videoId);

        ResponseEntity<GetEncodingStatusResponse> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, GetEncodingStatusResponse.class);

        HttpStatus statusCode = response.getStatusCode();

        if (response.getBody() == null) {
            //To-Do 예외 처리
            throw new MediaServerException();
        } else if (statusCode == HttpStatus.OK) {
            return response.getBody().getEntireJobStatus();
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            //To-Do 예외처리
            throw new MediaServerException();
        } else {
            //To-Do 예외처리
            throw new MediaServerException();
        }
    }

    @Override
    public void deleteVideo(UUID videoId) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");

        String url = String.format("%s/media/vods/%s", mediaServerUrl, videoId);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        HttpStatus statusCode = response.getStatusCode();

        if (statusCode == HttpStatus.OK) {
            return;
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            //To-Do 예외처리
            throw new MediaServerException();
        } else {
            //To-Do 예외처리
            throw new MediaServerException();
        }

    }

    @Override
    public void patchThumbnail(UUID videoId, MultipartFile thumbnailFile) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");
        Assert.notNull(thumbnailFile,"thumbnailFile must not be null");

        String url = String.format("%s/media/vods/%s/thumbnail", mediaServerUrl, videoId);

        ThumbnailPatchRequest body = ThumbnailPatchRequest.of(videoId, thumbnailFile);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PATCH, body, Void.class);

        HttpStatus statusCode = response.getStatusCode();

        if (statusCode == HttpStatus.OK) {
            return;
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            //To-Do 예외처리
            throw new MediaServerException();
        } else {
            //To-Do 예외처리
            throw new MediaServerException();
        }



    }

}
