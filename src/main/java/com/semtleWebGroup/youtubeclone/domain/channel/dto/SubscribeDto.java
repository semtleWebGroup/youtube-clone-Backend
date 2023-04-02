package com.semtleWebGroup.youtubeclone.domain.channel.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Subscribe;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link Subscribe} entity
 */
@Data
public class SubscribeDto implements Serializable {
    private final Integer id;
    @NotNull
    private final ChannelDto subscriber;
    @NotNull
    private final ChannelDto channel;
}