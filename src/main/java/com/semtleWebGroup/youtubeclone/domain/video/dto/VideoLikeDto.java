package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelDto;
import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoLike;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link VideoLike} entity
 */
@Data
public class VideoLikeDto implements Serializable {
    private final Integer id;
    @NotNull
    private final ChannelDto channelChannelid;
    @NotNull
    private final VideoDto videoVideoid;
}