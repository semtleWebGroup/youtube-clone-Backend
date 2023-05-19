package com.semtleWebGroup.youtubeclone.domain.channel.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.*;

/**
 * package :  com.semtleWebGroup.youtubeclone.domain.channel.dto
 * fileName : ChannelResponse
 * author :  ShinYeaChan
 * date : 2023-05-19
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ChannelResponse {
    private Channel channel;
    private String refreshedToken;
}
