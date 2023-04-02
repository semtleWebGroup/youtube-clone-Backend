package com.semtleWebGroup.youtubeclone.domain.video.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="video")
@Getter
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "videoid", updatable = false)
    private Long id;

    @Column(nullable = false, length=45)
    private String title;

    @Column(length=45)
    private String description;

    private Byte thumbImg;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    private int videoSec;
    private int viewCount;

    @Builder
    public Video(Long id, String title, String description, Byte thumbImg, int videoSec) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbImg = thumbImg;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
        this.videoSec = videoSec;
        this.viewCount = 0;

    }
}
