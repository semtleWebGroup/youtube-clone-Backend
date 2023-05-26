package com.semtleWebGroup.youtubeclone.domain.video.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoResponse;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoUploadDto;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MockedMediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.ProdMediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.test_super.MockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VideoServiceTest extends MockTest {
    private static VideoService videoService;
    private static VideoRepository videoRepository;
    private static MediaServerSpokesman mediaServerSpokesman;

    @BeforeAll
    public static void set() {
        videoRepository = Mockito.mock(VideoRepository.class);
        mediaServerSpokesman = Mockito.mock(MockedMediaServerSpokesman.class);
        videoService = new VideoService(videoRepository, mediaServerSpokesman);
    }

    @Nested
    @DisplayName("upload 메서드")
    class upload {
        @Test
        @DisplayName("upload 테스트 - 썸네일 O")
        void testUpload() {
            // given
            Channel channel = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            MultipartFile videoFile = new MockMultipartFile("test.mp4", "test".getBytes());
            MultipartFile thumbImg = new MockMultipartFile("test.jpg", "test".getBytes());
            VideoUploadDto dto = VideoUploadDto.builder()
                    .channel(channel)
                    .videoFile(videoFile)
                    .thumbImg(thumbImg)
                    .build();

            Video createdVideo = new Video();

            when(videoRepository.save(any(Video.class))).thenReturn(createdVideo);

            // when
            VideoResponse videoResponse = videoService.upload(dto);

            // then
            assertEquals(createdVideo.getId(), videoResponse.getVideoId());
            assertEquals(1, channel.getVideos().size()); // channel에 video가 들어가는지 확인
        }
    }

    @Nested
    @DisplayName("delete 메서드")
    class delete {
        @Test
        @DisplayName("delete 테스트 - 성공")
        void delete() {
            // given
            Video video = new Video();
            Channel channel = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            channel.addVideo(video);
            assertEquals(1, channel.getVideos().size());

            when(videoRepository.findById(video.getId())).thenReturn(Optional.of(video));

            // when
            VideoResponse videoResponse = videoService.delete(video.getId(), channel);

            // then
            assertEquals(0, channel.getVideos().size());
        }
    }



}
