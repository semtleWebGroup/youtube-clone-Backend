package com.semtleWebGroup.youtubeclone.domain.video.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import lombok.*;
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
public class VideoInfo extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "video_info_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "videoid", nullable = false)
    private Video video;

    @Column(nullable = false, length=45)
    private String title;

    @Column(length=45)
    private String description;

    @Lob
    private Blob thumbImg;

    private int viewCount;

    @Builder
    public VideoInfo(String title, String description, Video video) {
        this.title = title;
        this.description = description;
        this.video = video;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void update(String title, String description, Blob thumbImg) {
        this.title = title;
        this.description = description;
        this.thumbImg = thumbImg;
    }

    public void incrementViewCount() {
        this.viewCount += 1;
    }

}
