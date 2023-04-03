package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelDto;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link Video} entity
 */
@Data
public class VideoDto implements Serializable {
    private final Long id;
    private final byte[] thumbImg;
    @Size(max = 45)
    private final String title;
    @Size(max = 45)
    private final String description;
    private final Instant createdTime;
    private final Instant updatedTime;
    private final Integer videoSec;
    private final Integer viewCount;
    @NotNull
    private final ChannelDto channelChannelid;


}