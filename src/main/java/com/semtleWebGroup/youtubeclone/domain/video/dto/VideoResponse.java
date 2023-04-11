package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoResponse {
    private UUID videoId;
    private String title;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public VideoResponse(VideoInfo videoInfo) {
        this.videoId = videoInfo.getVideo().getVideoId();
        this.title = videoInfo.getTitle();
        this.description = videoInfo.getDescription();
        this.createdTime = videoInfo.getCreatedTime();
        this.updatedTime = videoInfo.getUpdatedTime();
    }
}
