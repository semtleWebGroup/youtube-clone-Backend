package com.semtleWebGroup.youtubeclone.domain.video.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;

@Entity
@Table(name="video_info")
@Getter
@NoArgsConstructor
public class VideoInfo extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "videoid", updatable = false)
    private Long id;

    @Column(nullable = false, length=45)
    private String title;

    @Column(length=45)
    private String description;

    @Lob
    private Blob thumbImg;

    private int videoSec;
    private int viewCount;

    @Builder
    public VideoInfo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateThumbImg(Blob thumbImg) {
        this.thumbImg = thumbImg;
    }

}
