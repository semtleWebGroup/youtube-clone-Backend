package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
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
    
    private Channel channel;
    
    @Builder
    public VideoUploadDto(Channel channel, MultipartFile videoFile, MultipartFile thumbImg) {
        this.channel = channel;
        this.videoFile = videoFile;
        this.thumbImg = thumbImg;
    }
}