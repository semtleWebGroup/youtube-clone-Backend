package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoListResponse {

    private UUID id;
    private String title;

    private byte[] thumbnail;

    private byte[] channelImg;

    private String channelName;

    private int viewCount;

    private int videoSec;

    @CreatedDate
    private LocalDateTime createdTime;

    @Builder
    public VideoListResponse(String title, String channelName, int viewCount, int videoSec) {
        this.title = title;
        this.channelName = channelName;
        this.viewCount = viewCount;
        this.videoSec = videoSec;
    }
}