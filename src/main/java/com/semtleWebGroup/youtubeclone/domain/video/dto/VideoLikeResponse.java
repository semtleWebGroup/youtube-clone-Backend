package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoLikeResponse {
    private boolean isLike;
    private int likeCount;

    public VideoLikeResponse(boolean isLike, int likeCount) {
        this.isLike = isLike;
        this.likeCount = likeCount;
    }
}
