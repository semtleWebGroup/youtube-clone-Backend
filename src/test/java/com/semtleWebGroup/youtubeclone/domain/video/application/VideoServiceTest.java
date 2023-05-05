package com.semtleWebGroup.youtubeclone.domain.video.application;

import com.semtleWebGroup.youtubeclone.domain.channel.application.ChannelService;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.test_super.MockTest;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

public class VideoServiceTest extends MockTest {
    private static VideoService videoService;
    private static VideoRepository videoRepository;
    private static MediaServerSpokesman mediaServerSpokesman;

    @BeforeAll
    public static void set() {
        videoRepository = Mockito.mock(VideoRepository.class);
        mediaServerSpokesman = Mockito.mock(MediaServerSpokesman.class);
        videoService = new VideoService(videoRepository, mediaServerSpokesman);
    }

}
