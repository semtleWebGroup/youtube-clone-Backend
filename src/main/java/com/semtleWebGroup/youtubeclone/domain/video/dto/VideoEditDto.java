package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoEditDto {
    private UUID videoId;
    private String title;
    private String description;
    private Channel channel;

    @Builder
    public VideoEditDto(Channel channel, UUID videoId, String title, String description) {
        this.channel = channel;
        this.videoId = videoId;
        this.title = title;
        this.description = description;
    }
}
