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
import java.util.HashSet;
import java.util.Set;
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
    private UUID id;

    @Column(length=45)
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

    @OneToMany(fetch=FetchType.LAZY)
    private Set<Channel> likedChannels = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "video")
    private Set<VideoLike> likes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Builder
    public Video(Channel channel) {
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

    public int getLikeCount() { return this.likedChannels.size(); }

    public Boolean isLike(Channel channel) {
        return channel == null? false : this.likedChannels.contains(channel);
    }
}