package com.semtleWebGroup.youtubeclone.domain.video_media.dto.request;

import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class ThumbnailPatchRequest extends HttpEntity<MultiValueMap<String, Object>> {
    private ThumbnailPatchRequest(MultiValueMap<String, Object> headers) {
        super(headers);
    }

    public static ThumbnailPatchRequest of(UUID videoId, MultipartFile thumbnail){
        MultiValueMap<String, Object> body = new org.springframework.util.LinkedMultiValueMap<>();
        body.add("videoId", videoId.toString());
        body.add("thumbnail", thumbnail.getResource());
        return new ThumbnailPatchRequest(body);
    }

}
