package com.semtleWebGroup.youtubeclone.domain.channel.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChannelRequest {
    private ChannelProfile channelProfile;
    private MultipartFile profile_img;
}
