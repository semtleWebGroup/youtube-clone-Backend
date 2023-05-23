package com.semtleWebGroup.youtubeclone.domain.video_media.service;

import com.semtleWebGroup.youtubeclone.domain.video_media.dto.response.GetEncodingStatusResponse;
import com.semtleWebGroup.youtubeclone.global.error.exception.MediaServerException;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class MockedMediaServerSpokesman implements MediaServerSpokesman {

    @Override
    public double sendEncodingRequest(MultipartFile videoFile, UUID videoId, @Nullable MultipartFile thumbnailFile) throws MediaServerException {
        Assert.notNull(videoFile, "videoFile must not be null");
        Assert.notNull(videoId, "videoId must not be null");
        return 100.0;
    }

    @Override
    public GetEncodingStatusResponse.EncodingStatus getEncodingStatus(UUID videoId) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");
        return GetEncodingStatusResponse.EncodingStatus.FINISHED;
    }

    @Override
    public void deleteVideo(UUID videoId) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");

    }

    @Override
    public void patchThumbnail(UUID videoId, MultipartFile thumbnailFile) throws MediaServerException {
        Assert.notNull(videoId,"videoId must not be null");
        Assert.notNull(thumbnailFile,"thumbnailFile must not be null");

    }
}
