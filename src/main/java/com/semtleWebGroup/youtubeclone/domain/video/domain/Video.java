package com.semtleWebGroup.youtubeclone.domain.video.domain;

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
    private Integer id;

    @Column(nullable = false, length=45)
    private String title;

    @Column(length=45)
    private String description;

    private String thumbImg;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    private int videoSec;
    private int viewCount;

    @Builder
    public Video(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }
}
