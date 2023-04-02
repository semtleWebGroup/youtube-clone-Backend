package com.semtleWebGroup.youtubeclone.domain.channel.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChannelProfile {
    @NotNull(message = "title은 null이 될 수 없습니다.")
    @Size(max = 15, message = "title은 15글자를 넘길 수 없습니다.")
    private String title;

    @Size(max = 70, message = "description은 70글자를 넘길 수 없습니다.")
    private String description;
}
