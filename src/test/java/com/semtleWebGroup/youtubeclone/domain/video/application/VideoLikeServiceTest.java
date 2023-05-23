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
            Video video = Video.builder()
                    .channel(channel)
                    .build();
            VideoLike videoLike = new VideoLike();

            when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));
            when(videoRepository.save(video)).thenReturn(video);
            when(videoLikeRepository.save(videoLike)).thenReturn(videoLike);

            // when
            VideoLikeResponse videoLikeResponse = videoLikeService.add(video.getId(), channel);

            // then
            assertEquals(1, videoLikeResponse.getLikeCount());
            assertTrue(videoLikeResponse.isLike());
            assertEquals(1, video.getLikes().size());               // video에 like가 들어갔는지 확인
            assertEquals(1, channel.getVideoLikeLists().size());    // channel에 video가 들어갔는지 확인
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
            Video video = new Video();
            VideoLike videoLike = VideoLike.builder()
                    .channel(channel)
                    .build();
            channel.addVideo(video);
            video.likeVideo(videoLike);
            channel.addVideo(video);
            assertEquals(1, channel.getVideoLikeLists().size());
            assertEquals(1, video.getLikes().size());

            when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));
            when(videoRepository.save(video)).thenReturn(video);
            when(videoLikeRepository.findByVideoAndChannel(video, channel)).thenReturn(videoLike);

            // when (1번 삽입 & 2번 제거 테스트)
            videoLikeService.delete(video.getId(), channel);
            VideoLikeResponse videoLikeResponse = videoLikeService.delete(video.getId(), channel);

            // then
            assertEquals(0, videoLikeResponse.getLikeCount());
            assertTrue(!videoLikeResponse.isLike());
            assertEquals(0, channel.getVideoLikeLists().size());
            assertEquals(0, video.getLikes().size());
        }
    }
}
