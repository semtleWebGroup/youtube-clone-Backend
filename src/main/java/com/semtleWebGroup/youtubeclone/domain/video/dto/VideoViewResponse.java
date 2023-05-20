package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.sql.Blob;
import java.sql.SQLException;
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
    private byte[] channelProfileImg;
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
            Boolean isLike
            //ArrayList<String> qualityList // TODO
    ) {
        this.videoId = video.getId();
        this.videoSec = video.getVideoSec();
        this.title = video.getTitle();
        this.createdTime = video.getCreatedTime();
        this.description = video.getDescription();
        this.viewCount = video.getViewCount();
        
        this.channelId = video.getChannel().getId();
        this.channelName = video.getChannel().getTitle();
        this.channelProfileImg = convertBlobToBytes(video.getChannel().getChannelImage());
        this.channelSubscriberCount = video.getChannel().getSubscribers().size();
        
        this.likeCount = video.getLikeCount();
        this.isLike = isLike;
        
        ArrayList<String> qualityList = new ArrayList<String>();
        qualityList.add("1080p");
        qualityList.add("360p");
        this.qualityList = qualityList;
    }
    
    private byte[] convertBlobToBytes (Blob blob) {
        try {
            if (blob != null) blob.getBytes(1, (int) blob.length());
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO: Handle Exception
        }
        return null;
    }
}