package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
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
                .orElseThrow(()->new EntityNotFoundException(
                        String.format("%d is not found.", channelId)
                ));
        Channel subscribedChannel = channelRepository.findById(subscribedChannelId)
                .orElseThrow(()->new EntityNotFoundException(
                        String.format("%d is not found.", subscribedChannelId)
                ));

        channel.getSubscribedChannels().add(subscribedChannel);
        channelRepository.save(channel);
    }

    @Transactional
    public void unsubscribe(Long channelId, Long subscribedChannelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(()->new EntityNotFoundException(
                        String.format("%d is not found.", channelId)
                ));
        Channel subscribedChannel = channelRepository.findById(subscribedChannelId)
                .orElseThrow(()->new EntityNotFoundException(
                        String.format("%d is not found.", subscribedChannelId)
                ));
        channel.getSubscribedChannels().remove(subscribedChannel);
        channelRepository.save(channel);
    }

    public Set<Channel> getSubscribedChannels(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(()->new EntityNotFoundException(
                        String.format("%d is not found.", channelId)
                ));

        return channel.getSubscribedChannels();
    }

    public Long getCountOfSubscribers(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(()->new EntityNotFoundException(
                        String.format("%d is not found.", channelId)
                ));

        return (long) channel.getSubscribers().size();
    }

    public boolean isSubscribed(Long channelId, Long subscriberId) {
        // channelId와 subscriberId로 해당 채널과 구독자를 찾아온다.
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(()->new EntityNotFoundException(
                        String.format("%d is not found.", channelId)
                ));
        Channel subscriber = channelRepository.findById(subscriberId)
                .orElseThrow(()->new EntityNotFoundException(
                        String.format("%d is not found.", subscriberId)
                ));

        // 해당 구독자가 채널을 구독하고 있는지 여부를 판단하여 반환
        return channel.getSubscribers().contains(subscriber);
    }
}
