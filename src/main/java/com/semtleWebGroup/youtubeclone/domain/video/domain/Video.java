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
public class Video extends BaseTime{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "videoid", updatable = false)
    private Integer id;

    @Column(nullable = false, length=45)
    private String title;

    @Column(length=45)
    private String description;

    private String thumbImg;

    private int videoSec;

    private int viewCount;

    @Builder
    public Video(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void update(String title, String description, String thumbImg) {
        this.title = title;
        this.description = description;
        this.thumbImg = thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }
}
