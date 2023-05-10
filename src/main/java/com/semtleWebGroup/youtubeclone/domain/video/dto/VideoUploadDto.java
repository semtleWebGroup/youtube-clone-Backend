package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoUploadDto {
    private MultipartFile videoFile;
    private MultipartFile thumbImg;

    @Builder
    public VideoUploadDto(MultipartFile videoFile, MultipartFile thumbImg) {
        this.videoFile = videoFile;
        this.thumbImg = thumbImg;
    }
}
