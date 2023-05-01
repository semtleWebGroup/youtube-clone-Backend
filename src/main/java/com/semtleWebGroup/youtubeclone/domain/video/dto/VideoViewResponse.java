package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
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
            VideoInfo videoInfo,
            VideoLikeResponse videoLike
    ) {
        this.videoId = videoInfo.getVideo().getVideoId();
        this.videoSec = videoInfo.getVideo().getVideoSecond();

        this.channelId = videoInfo.getVideo().getChannel().getId();
        this.channelName = videoInfo.getVideo().getChannel().getTitle();
//        this.channelProfileImg = videoInfo.getVideo().getChannel().getProfileImg(); // TODO
        this.channelProfileImg = null;
//        this.channelSubscriberCount = videoInfo.getVideo().getChannel().getSubscriberCount(); // TODO
        this.channelSubscriberCount = 0;

        this.title = videoInfo.getTitle();
        this.createdTime = videoInfo.getCreatedTime();
        this.description = videoInfo.getDescription();
        this.viewCount = videoInfo.getViewCount();

        this.likeCount = videoLike.getLikeCount();
        this.isLike = videoLike.isLike(); // ? 왜 getIsLike가 아니라 isLike인지 잘 모르겠음.

        ArrayList<String> qualityList = new ArrayList<String>();
        qualityList.add("1080p");
        qualityList.add("360p");
        this.qualityList = qualityList;
    }
}