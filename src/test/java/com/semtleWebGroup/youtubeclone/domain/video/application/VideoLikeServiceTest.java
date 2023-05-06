package com.semtleWebGroup.youtubeclone.domain.video.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoLike;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoLikeRepository;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoLikeService;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.test_super.MockTest;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VideoLikeServiceTest extends MockTest {
    private static VideoLikeRepository videoLikeRepository;
    private static VideoLikeService videoLikeService;
    private static VideoService videoService;

    private static ChannelRepository channelRepository;
    private static VideoRepository videoRepository;

    @BeforeAll
    public static void set() {
        channelRepository = Mockito.mock(ChannelRepository.class);
        videoRepository = Mockito.mock(VideoRepository.class);
        videoLikeRepository = Mockito.mock(VideoLikeRepository.class);
        MediaServerSpokesman mediaServerSpokesman = new MediaServerSpokesman();
        videoService = new VideoService(videoRepository, mediaServerSpokesman);
        videoLikeService = new VideoLikeService(videoLikeRepository, videoService);
    }

    @Nested
    @DisplayName("add 메서드")
    class add {
        @Test
        @DisplayName("add 테스트 - 성공")
        void testAdd() {
            // given
            Channel channel = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            UUID videoId = UUID.randomUUID();
            Video video = Video.builder()
                    .channel(channel)
                    .build();
            VideoLike videoLike = VideoLike.builder()
                    .channel(channel)
                    .video(video)
                    .build();

            when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
            when(videoRepository.save(video)).thenReturn(video);
            when(videoLikeRepository.save(videoLike)).thenReturn(videoLike);

            // when
            VideoLikeResponse videoLikeResponse = videoLikeService.add(videoId, channel);

            // then
            assertEquals(1, videoLikeResponse.getLikeCount());
            assertTrue(videoLikeResponse.isLike());
        }
    }

    @Nested
    @DisplayName("delete 메서드")
    class delete {
        @Test
        @DisplayName("delete 테스트 - 성공")
        void testAdd() {
            // given
            Channel channel = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            UUID videoId = UUID.randomUUID();
            Video video = Video.builder()
                    .channel(channel)
                    .build();
            VideoLike videoLike = VideoLike.builder()
                    .channel(channel)
                    .video(video)
                    .build();
            video.getLikes().add(videoLike);

            when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
            when(videoRepository.save(video)).thenReturn(video);
            when(videoLikeRepository.findByVideoAndChannel(video, channel)).thenReturn(videoLike);

            // when
            videoLikeService.delete(videoId, channel);
            VideoLikeResponse videoLikeResponse = videoLikeService.delete(videoId, channel);

            // then
            assertEquals(0, videoLikeResponse.getLikeCount());
            assertTrue(!videoLikeResponse.isLike());
        }
    }
}
