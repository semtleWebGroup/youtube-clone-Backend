package com.semtleWebGroup.youtubeclone.domain.video.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="video_like")
@Getter
@NoArgsConstructor
public class VideoLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_like_id", columnDefinition = "BIGINT", nullable = false)
    private Long videoLikeId;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @Builder
    public VideoLike(Channel channel, Video video) {
        this.channel = channel;
        this.video = video;
    }

}
