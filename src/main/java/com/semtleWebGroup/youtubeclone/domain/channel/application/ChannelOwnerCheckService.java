package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.exception.ForbiddenException;
import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class ChannelOwnerCheckService {
    public boolean checkChannelOwner(Channel channel, Long memberId){
        if (!channel.getMember().getId().equals(memberId)) {
            throw new ForbiddenException(ErrorCode.ACCESS_DENIED);
        }
        return true;
    }
}
