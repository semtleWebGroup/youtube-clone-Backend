package com.semtleWebGroup.youtubeclone.domain.video.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="video")
@Getter
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "video_id",columnDefinition = "BINARY(16)", nullable = false)
    private UUID videoId;

    @Column(nullable = false, length=45)
    private String title;

    @Column(length=45)
    private String description;

    private Blob thumbImg;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    private int viewCount;

    private Long videoSec;

    private MediaServerSpokesman.EncodingStatus status;

    @ManyToOne
    @JoinColumn(name = "channel_id",nullable = false)
    private Channel channel;

    @Builder
    public Video(String title, String description, Blob thumbImg, Channel channel) {
        this.title = title;
        this.description = description;
        this.thumbImg = thumbImg;
        this.viewCount = 0;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
        this.channel = channel;
    }

    public void update(String title, String description, Blob thumbImg) {
        this.update(title, description);
        this.thumbImg = thumbImg;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
        setUpdatedTime(LocalDateTime.now());
    }

    public void incrementViewCount() {
        this.viewCount += 1;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
