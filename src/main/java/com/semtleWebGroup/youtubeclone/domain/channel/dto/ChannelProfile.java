package com.semtleWebGroup.youtubeclone.domain.channel.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChannelProfile {
    @NotBlank(message = "title은 빈값이 될 수 없습니다.")
    @Size(max = 15, message = "title은 15글자를 넘길 수 없습니다.")
    private String title;

    @Size(max = 70, message = "description은 70글자를 넘길 수 없습니다.")
    private String description;
}
