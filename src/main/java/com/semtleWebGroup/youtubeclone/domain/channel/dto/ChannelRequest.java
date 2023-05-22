package com.semtleWebGroup.youtubeclone.domain.channel.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChannelRequest {
    @Valid
    private ChannelProfile channelProfile;
    private MultipartFile profile_img;
}
