package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoViewResponse {
    // video
    private UUID videoId;
    private Long videoSec;

    // channel
    private Long channelId;
    private String channelName;
    private Byte[] channelProfileImg;
    private int channelSubscriberCount;

    // video info
    @NotEmpty()
    private String title;
    private String description;
    private LocalDateTime createdTime;
    private int viewCount;

    // video like
    private int likeCount;
    private boolean isLike;

    // video media
    private List<String> qualityList;

    @Builder
    public VideoViewResponse(
            Video video,
            VideoLikeResponse videoLike
    ) {
        this.videoId = video.getVideoId();
        this.videoSec = video.getVideoSec();

        this.channelId = video.getChannel().getId();
        this.channelName = video.getChannel().getTitle();
//        this.channelProfileImg = video.getChannel().getProfileImg(); // TODO
        this.channelProfileImg = null;
//        this.channelSubscriberCount = video.getVideo().getChannel().getSubscriberCount(); // TODO
        this.channelSubscriberCount = 0;

        this.title = video.getTitle();
        this.createdTime = video.getCreatedTime();
        this.description = video.getDescription();
        this.viewCount = video.getViewCount();

        this.likeCount = videoLike.getLikeCount();
        this.isLike = videoLike.isLike(); // ? 왜 getIsLike가 아니라 isLike인지 잘 모르겠음.

        ArrayList<String> qualityList = new ArrayList<String>();
        qualityList.add("1080p");
        qualityList.add("360p");
        this.qualityList = qualityList;
    }
}