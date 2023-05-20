package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoLikeResponse {
    private UUID videoId;
    private boolean isLike;
    private int likeCount;
    
    @Builder
    public VideoLikeResponse(UUID videoId, boolean isLike, int likeCount) {
        this.videoId = videoId;
        this.isLike = isLike;
        this.likeCount = likeCount;
    }
}