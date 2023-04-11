package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public VideoResponse(
            UUID videoId,
            String title,
            String description,
            LocalDateTime createTime,
            LocalDateTime updatedTime
    ) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.createdTime = createTime;
        this.updatedTime = updatedTime;
    }
}
