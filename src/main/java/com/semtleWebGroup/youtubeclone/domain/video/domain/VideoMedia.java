package com.semtleWebGroup.youtubeclone.domain.video.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "video_media")
public class VideoMedia {
    @Id
    @Column(name = "video_mediaid", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "video_videoid", nullable = false)
    private Video videoVideoid;

    @Size(max = 45)
    @NotNull
    @Column(name = "video_path", nullable = false, length = 45)
    private String videoPath;

    @Column(name = "quality")
    private Integer quality;

}