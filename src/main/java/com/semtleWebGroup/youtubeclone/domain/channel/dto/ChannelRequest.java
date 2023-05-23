package com.semtleWebGroup.youtubeclone.domain.channel.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class ChannelRequest {
    private ChannelProfile channelProfile;
    private MultipartFile profile_img;
}
