package com.semtleWebGroup.youtubeclone.domain.member.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.semtleWebGroup.youtubeclone.domain.member.domain.CommentLike} entity
 */
@Data
public class CommentLikeDto implements Serializable {
    private final Integer id;
    @NotNull
    private final ChannelDto channelChannelid;
    @NotNull
    private final CommentDto commentCommentid;
}