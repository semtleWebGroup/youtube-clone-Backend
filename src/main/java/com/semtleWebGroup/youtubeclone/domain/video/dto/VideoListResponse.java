package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class VideoListResponse {
    private String title;

    private byte[] thumbnail;

    private byte[] channelImg;

    private String channelName;

    private int viewCount;

    private int videoSec;

    @CreatedDate
    private LocalDateTime createdTime;

    public VideoListResponse(String title, String channelName, int viewCount, int videoSec) {
        this.title = title;
        this.channelName = channelName;
        this.viewCount = viewCount;
        this.videoSec = videoSec;
    }
}