package com.semtleWebGroup.youtubeclone.domain.video_media.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "video_media")
@Getter @Setter @EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoMedia {

    //외부에 노출하는 키는 UUID 로 하는게 좋다.
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "id",columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "width")
    private int width; //이거 기준으로 1080p 720p같은 것을 나눔
    @Column(name = "height")
    private int height;

    @Column(name = "frame")
    private int framePerSec;

    @Column(name = "audio_channel")
    private int audioChannel;

    @Column(name = "file_size")
    private long fileSize; //KB 단위

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "video_format")
    private String videoFormat;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
}