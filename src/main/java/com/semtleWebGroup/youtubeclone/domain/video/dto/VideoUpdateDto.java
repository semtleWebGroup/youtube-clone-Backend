package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoUpdateDto {
    private UUID videoId;
    private String title;
    private String description;

    @Builder
    public VideoUpdateDto(UUID videoId, String title, String description) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
    }
}
