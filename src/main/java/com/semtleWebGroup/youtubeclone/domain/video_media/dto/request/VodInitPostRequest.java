package com.semtleWebGroup.youtubeclone.domain.video_media.dto.request;

import org.springframework.http.HttpEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public class VodInitPostRequest extends HttpEntity<MultiValueMap<String, Object>> {

//    public VodInitPostRequest(String url, MultipartFile videoFile, @Nullable MultipartFile thumbnailFile) {
//        super(body);
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("videoFile", videoFile.getResource());
//        if (thumbnailFile != null) body.add("thumbnailFile", thumbnailFile.getResource());
//
//    }

    protected VodInitPostRequest(MultiValueMap<String, Object> headers) {
        super(headers);
    }

    public static VodInitPostRequest of(MultipartFile videoFile, @Nullable MultipartFile thumbnailFile) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("videoFile", videoFile.getResource());
        if (thumbnailFile != null) body.add("thumbnailFile", thumbnailFile.getResource());
        return new VodInitPostRequest(body);
    }
}
