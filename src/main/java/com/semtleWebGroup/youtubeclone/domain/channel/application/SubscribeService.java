package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    final private ChannelRepository channelRepository;

    @Transactional
    public void subscribe(Long channelId, Long subscribedChannelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널 아이디를 찾을 수 없음 : " + channelId));
        Channel subscribedChannel = channelRepository.findById(subscribedChannelId)
                .orElseThrow(()-> new IllegalArgumentException("구독할 채널 아이디를 찾을 수 없음 : " + subscribedChannelId));

        channel.getSubscribedChannels().add(subscribedChannel);
        channelRepository.save(channel);
    }

    @Transactional
    public void unsubscribe(Long channelId, Long subscribedChannelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널 아이디를 찾을 수 없음 : " + channelId));
        Channel subscribedChannel = channelRepository.findById(subscribedChannelId)
                .orElseThrow(()-> new IllegalArgumentException("구독할 채널 아이디를 찾을 수 없음 : " + subscribedChannelId));
        channel.getSubscribedChannels().remove(subscribedChannel);
        channelRepository.save(channel);
    }

    public Set<Channel> getSubscribedChannels(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(()-> new IllegalArgumentException("채널 아이디를 찾을 수 없음 : " + channelId));

        return channel.getSubscribedChannels();
    }

}
