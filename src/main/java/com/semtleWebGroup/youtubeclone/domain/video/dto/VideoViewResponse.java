package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoViewResponse {
    private UUID videoId;
    private Long channelId;
    private String channelName;
    private Byte[] channelProfileImg;
    private int channelSubscriberCount;
    @NotEmpty()
    private String title;
    private String description;
    private LocalDateTime createdTime;
    private Long videoSec;
    private int viewCount;
    private int likeCount;
    private boolean isLike;
    private List<String> qualityList;

    @Builder
    public VideoViewResponse(
            UUID videoId,
            Long channelId,
            String channelName,
            Byte[] channelProfileImg,
            int channelSubscriberCount,
            String title,
            String description,
            LocalDateTime createdTime,
            Long videoSec,
            int viewCount,
            int likeCount,
            boolean isLike,
            List<String> qualityList
            ) {
        this.videoId = videoId;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelProfileImg = channelProfileImg;
        this.channelSubscriberCount = channelSubscriberCount;
        this.title = title;
        this.createdTime = createdTime;
        this.description = description;
        this.videoSec = videoSec;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.qualityList = qualityList;
    }
}