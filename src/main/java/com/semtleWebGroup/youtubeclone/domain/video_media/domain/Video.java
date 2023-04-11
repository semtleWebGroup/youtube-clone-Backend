package com.semtleWebGroup.youtubeclone.domain.video_media.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "video")
@Getter
@Setter
@EqualsAndHashCode(of = "videoId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video {


    //외부에 노출되는 키는 UUID 로 하여 유추할 수 없도록 한다.
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "video_id",columnDefinition = "BINARY(16)", nullable = false)
    private UUID videoId;

    @Column(name = "video_second",nullable = false)
    private long videoSecond;

    @Column(name = "cashed",nullable = false)
    private boolean isCashed;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    public Video(long videoSecond, boolean isCashed, Channel channel) {
        this.videoSecond = videoSecond;
        this.isCashed = isCashed;
        this.channel = channel;
    }
}