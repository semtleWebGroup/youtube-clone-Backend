package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.test_super.MockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscribeServiceTest extends MockTest {

    // mock up
    private static SubscribeService subscribeService;
    private static ChannelRepository channelRepository;

    @BeforeAll
    public static void setMockChannelRepository() {
        channelRepository = Mockito.mock(ChannelRepository.class);
        subscribeService = new SubscribeService(channelRepository);
    }

    @Nested
    @DisplayName("subscribe 메서드")
    class subscribe {
        @Test
        @DisplayName("subscribe 테스트 - 성공")
        public void testSubscribe() {
            // given
            Long channelId = 1L;
            Long subscribedChannelId = 2L;

            Channel channel = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            Channel subscribedChannel = Channel.builder()
                    .title("Subscribed Title")
                    .description("Subscribed Description")
                    .build();

            when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
            when(channelRepository.findById(subscribedChannelId)).thenReturn(Optional.of(subscribedChannel));
            when(channelRepository.save(channel)).thenReturn(channel);

            // when
            subscribeService.subscribe(channelId, subscribedChannelId);

            // then
            verify(channelRepository, times(1)).save(channel);
            assertEquals(1, channel.getSubscribedChannels().size());
            assertTrue(channel.getSubscribedChannels().contains(subscribedChannel));
        }
    }

    @Nested
    @DisplayName("unsubscribe 메서드")
    class unsubscribe{
        @Test
        @DisplayName("unsubscribe 테스트 - 성공")
        public void testUnsubscribe () {
            // given
            Long channelId = 1L;
            Long subscribedChannelId = 2L;

            Channel channel = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            Channel subscribedChannel = Channel.builder()
                    .title("Subscribed Title")
                    .description("Subscribed Description")
                    .build();
            channel.getSubscribedChannels().add(subscribedChannel);

            when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
            when(channelRepository.findById(subscribedChannelId)).thenReturn(Optional.of(subscribedChannel));
            when(channelRepository.save(channel)).thenReturn(channel);

            // when
            subscribeService.unsubscribe(channelId, subscribedChannelId);

            // then
            verify(channelRepository, times(1)).save(channel);
            assertEquals(0, channel.getSubscribedChannels().size());
            assertFalse(channel.getSubscribedChannels().contains(subscribedChannel));
        }
    }

    @Nested
    @DisplayName("getSubscribedChannels 메서드")
    class getSubscribedChannels {
        @Test
        @DisplayName("getSubscribedChannels 테스트 - 성공")
        public void testGetSubscribedChannels() {
            // given
            Long channelId = 1L;
            Channel channel = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            Channel subscribedChannel1 = Channel.builder()
                    .title("Subscribed1 Title")
                    .description("Subscribed1 Description")
                    .build();
            Channel subscribedChannel2 = Channel.builder()
                    .title("Subscribed2 Title")
                    .description("Subscribed2 Description")
                    .build();
            Set<Channel> subscribedChannels = new HashSet<>();
            subscribedChannels.add(subscribedChannel1);
            subscribedChannels.add(subscribedChannel2);
            channel.setSubscribedChannels(subscribedChannels);

            when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

            // when
            Set<Channel> result = subscribeService.getSubscribedChannels(channelId);

            // then
            assertEquals(2, result.size());
            assertTrue(result.contains(subscribedChannel1));
            assertTrue(result.contains(subscribedChannel2));
        }
    }

    @Nested
    @DisplayName("getCountOfSubscribers 메서드")
    class getCountOfSubscribers {
        @Test
        @DisplayName("getCountOfSubscribers 테스트 - 성공")
        public void testGetCountOfSubscribers() {
            // given
            Long channelId = 1L;
            Channel channel = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            Channel subscriber1 = Channel.builder()
                    .title("Subscriber1 Title")
                    .description("Subscriber1 Description")
                    .build();
            Channel subscriber2 = Channel.builder()
                    .title("Subscriber2 Title")
                    .description("Subscriber2 Description")
                    .build();
            Set<Channel> subscribers = new HashSet<>();
            subscribers.add(subscriber1);
            subscribers.add(subscriber2);
            channel.setSubscribers(subscribers);

            when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

            // when
            Long result = subscribeService.getCountOfSubscribers(channelId);

            // then
            assertEquals(2L, result);
        }
    }

    @Nested
    @DisplayName("isSubscribed 메서드")
    class isSubscribed {
        @Test
        @DisplayName("isSubscribed 테스트 - 성공")
        public void testIsSubscribed() {
            Long channelId = 1L;
            Long subscriberId = 2L;

            Channel channel = Channel.builder()
                    .title("test channel")
                    .build();

            Channel subscriber = Channel.builder()
                    .title("test subscriber")
                    .build();

            Set<Channel> subscribers = new HashSet<>();
            subscribers.add(subscriber);
            channel.setSubscribers(subscribers);

            when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
            when(channelRepository.findById(subscriberId)).thenReturn(Optional.of(subscriber));

            assertTrue(subscribeService.isSubscribed(channelId, subscriberId));
        }
    }

}
