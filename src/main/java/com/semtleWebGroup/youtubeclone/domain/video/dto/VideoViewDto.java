package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoViewDto {
    private Long videoId;
    private Long channelId;
    private String channelName;
    private Byte[] channelProfileImg;
    private int channelSubscriberCount;
    @NotEmpty()
    private String title;

    private String description;

    private LocalDateTime createdTime;

    private int videoSec;
    private int viewCount;
    private int likeCount;
    private boolean isLike;
    private List<String> qualityList;

    @Builder
    public VideoViewDto(
            Long videoId,
            Long channelId,
            String channelName,
            Byte[] channelProfileImg,
            int channelSubscriberCount,
            String title,
            String description,
            int videoSec,
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
        this.createdTime = LocalDateTime.now();
        this.description = description;
        this.videoSec = videoSec;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.qualityList = qualityList;
    }
}