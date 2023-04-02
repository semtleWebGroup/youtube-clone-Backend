package com.semtleWebGroup.youtubeclone.domain.member.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelDto;
import com.semtleWebGroup.youtubeclone.domain.member.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.semtleWebGroup.youtubeclone.domain.member.domain.Comment} entity
 */
@Data
public class CommentDto implements Serializable {
    private final Integer id;
    @Size(max = 45)
    @NotNull
    private final String contents;
    @NotNull
    private final Instant createdTime;
    @NotNull
    private final Instant updatedTime;
    @NotNull
    private final VideoDto videoVideoid;
    @NotNull
    private final ChannelDto channelChannelid;
    @NotNull
    private final Comment commentCommentid;
}