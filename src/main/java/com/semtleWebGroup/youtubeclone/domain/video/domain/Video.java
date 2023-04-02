package com.semtleWebGroup.youtubeclone.domain.video.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "video")
public class Video {
    @Id
    @Column(name = "videoid", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "thumb_img")
    private byte[] thumbImg;

    @Size(max = 45)
    @Column(name = "title", length = 45)
    private String title;

    @Size(max = 45)
    @Column(name = "description", length = 45)
    private String description;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Column(name = "video_sec")
    private Integer videoSec;

    @Column(name = "view_count")
    private Integer viewCount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_channelid", nullable = false)
    private Channel channelChannelid;

}