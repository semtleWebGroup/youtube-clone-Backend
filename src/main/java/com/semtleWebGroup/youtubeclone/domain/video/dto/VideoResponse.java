package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
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

    public VideoResponse(Video video) {
        this.videoId = video.getVideoId();
        this.title = video.getTitle();
        this.description = video.getDescription();
        this.createdTime = video.getCreatedTime();
        this.updatedTime = video.getUpdatedTime();
    }
}
