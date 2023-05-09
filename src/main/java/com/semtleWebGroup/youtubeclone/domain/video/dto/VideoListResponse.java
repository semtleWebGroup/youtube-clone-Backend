package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoListResponse {

    private UUID id;
    private String title;

    private byte[] thumbImg;

    private byte[] channelImg;

    private String channelName;

    private int viewCount;

    private Long videoSec;

    private LocalDateTime createdTime;

    @Builder
    public VideoListResponse(Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.thumbImg = convertBlobToBytes(video.getThumbImg());
        this.channelImg = convertBlobToBytes(video.getChannel().getChannelImage());
        this.channelName = video.getChannel().getTitle();
        this.viewCount = video.getViewCount();
        this.videoSec = video.getVideoSec();
        this.createdTime = video.getCreatedTime();
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