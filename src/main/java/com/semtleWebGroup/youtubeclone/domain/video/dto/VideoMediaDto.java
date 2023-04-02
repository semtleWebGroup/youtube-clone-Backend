package com.semtleWebGroup.youtubeclone.domain.video.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.semtleWebGroup.youtubeclone.domain.video.domain.VideoMedia} entity
 */
@Data
public class VideoMediaDto implements Serializable {
    private final Integer id;
    @NotNull
    private final VideoDto videoVideoid;
    @Size(max = 45)
    @NotNull
    private final String videoPath;
    private final Integer quality;
}