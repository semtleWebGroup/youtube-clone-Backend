package com.semtleWebGroup.youtubeclone.domain.video_media.service;

import com.semtleWebGroup.youtubeclone.domain.video_media.dto.request.ThumbnailPatchRequest;
import com.semtleWebGroup.youtubeclone.domain.video_media.dto.request.VodInitPostRequest;
import com.semtleWebGroup.youtubeclone.domain.video_media.dto.response.GetEncodingStatusResponse;
import com.semtleWebGroup.youtubeclone.domain.video_media.dto.response.VodInitPostResponse;
import com.semtleWebGroup.youtubeclone.global.error.exception.MediaServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
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
        ResponseEntity<VodInitPostResponse> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpRequest,
                    VodInitPostResponse.class);
        } catch (HttpClientErrorException e){
            log.error(e.getLocalizedMessage());
            throw new MediaServerException(e);
        }
        log.debug("response from media server: {}", response.getStatusCodeValue());
        return response.getBody().getVideoLength();
    }

    @Override
    public GetEncodingStatusResponse.EncodingStatus getEncodingStatus(UUID videoId) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");

        String url = String.format("%s/media/vods/%s/encoding/status", mediaServerUrl, videoId);
        ResponseEntity<GetEncodingStatusResponse> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, GetEncodingStatusResponse.class);
        } catch (HttpClientErrorException e){
            log.error(e.getLocalizedMessage());
            throw new MediaServerException(e);
        }
        log.debug("response from media server: {}", response.getStatusCodeValue());
        return response.getBody().getEntireJobStatus();
    }

    @Override
    public void deleteVideo(UUID videoId) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");

        String url = String.format("%s/media/vods/%s", mediaServerUrl, videoId);

        ResponseEntity<Void> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        } catch (HttpClientErrorException e){
            log.error(e.getLocalizedMessage());
            throw new MediaServerException(e);
        }
        log.debug("response from media server: {}", response.getStatusCodeValue());


    }

    @Override
    public void patchThumbnail(UUID videoId, MultipartFile thumbnailFile) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");
        Assert.notNull(thumbnailFile,"thumbnailFile must not be null");

        String url = String.format("%s/media/vods/%s/thumbnail", mediaServerUrl, videoId);

        ThumbnailPatchRequest body = ThumbnailPatchRequest.of(videoId, thumbnailFile);

        ResponseEntity<Void> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.PATCH, body, Void.class);
        } catch (HttpClientErrorException e){
            log.error(e.getLocalizedMessage());
            throw new MediaServerException(e);
        }
        log.debug("response from media server: {}", response.getStatusCodeValue());



    }

}
