package com.semtleWebGroup.youtubeclone.domain.video.domain;

import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;

@Entity
@Table(name="video_info")
@Getter
@NoArgsConstructor
public class VideoInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "video_info_id", updatable = false)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "videoid", nullable = false)
    private Video video;

    @Column(nullable = false, length=45)
    private String title;

    @Column(length=45)
    private String description;

    @Lob
    private Blob thumbImg;

    private int viewCount;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;


    @Builder
    public VideoInfo(String title, String description, Video video, Blob thumbImg) {
        this.video = video;
        this.title = title;
        this.description = description;
        this.thumbImg = thumbImg;
        this.viewCount = 0;
        this.createdTime = LocalDateTime.now();
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
