package com.semtleWebGroup.youtubeclone.domain.video_media.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cachedVideoMedia")
@Getter @EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CachedVideoMedia {

    @Id
    private Long id;

    @Column(name = "file_path",nullable = false, unique = true)
    private String filePath;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "video_id",nullable = false)
    private Video rootVideo;

    @Column(name = "created_time",nullable = false)
    private LocalDateTime createdTime;

    @PrePersist
    public void prePersist() {
        this.createdTime = LocalDateTime.now();
    }
    public CachedVideoMedia(String filePath, Video rootVideo) {
        this.filePath = filePath;
        this.rootVideo = rootVideo;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setRootVideo(Video rootVideo) {
        this.rootVideo = rootVideo;
    }
}
