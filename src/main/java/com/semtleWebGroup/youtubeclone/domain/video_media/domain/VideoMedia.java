package com.semtleWebGroup.youtubeclone.domain.video_media.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "video_media",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id","width","height","frame","audio_channel","file_size","video_format"} , name = "video_media_property_unique")
})
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoMedia {

    //외부에 노출하는 키는 UUID 로 하는게 좋다.
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "id",columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "width",nullable = false)
    private int width; //이거 기준으로 1080p 720p같은 것을 나눔
    @Column(name = "height",nullable = false)
    private int height;

    @Column(name = "frame",nullable = false)
    private int framePerSec;

    @Column(name = "audio_channel",nullable = false)
    private int audioChannel;

    @Column(name = "file_size",nullable = false)
    private long fileSize; //KB 단위


    @Column(name = "video_format",nullable = false)
    private String videoFormat;
    @Column(name = "file_path",nullable = false, unique = true)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "video_id",nullable = false)
    private Video rootVideo;

    @Builder()
    public VideoMedia(final int width,final int height,final int framePerSec,final int audioChannel,final long fileSize,final String filePath, final String videoFormat, final Video rootVideo) {
        this.width = width;
        this.height = height;
        this.framePerSec = framePerSec;
        this.audioChannel = audioChannel;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.videoFormat = videoFormat;
        this.rootVideo = rootVideo;
    }
}