package com.semtleWebGroup.youtubeclone.domain.video.domain;

import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="video_info")
@Getter
@NoArgsConstructor
public class VideoInfo {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "video_info_id",columnDefinition = "BINARY(16)", nullable = false)
    private UUID videoInfoId;

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

    @OneToOne
    @JoinColumn(name = "video_id",nullable = false)
    private Video video;

    @Builder
    public VideoInfo(String title, String description, Video video, Blob thumbImg) {
        this.video = video;
        this.title = title;
        this.description = description;
        this.thumbImg = thumbImg;
        this.viewCount = 0;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
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
