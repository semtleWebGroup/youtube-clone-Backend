package com.semtleWebGroup.youtubeclone.domain.channel.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.member.dto.MemberDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link Channel} entity
 */
@Data
public class ChannelDto implements Serializable {
    private final Integer id;
    @Size(max = 15)
    @NotNull
    private final String title;
    @Size(max = 70)
    private final String description;
    @Size(max = 60)
    @NotNull
    private final String profileImg;
    @NotNull
    private final MemberDto memberMemberid;
}