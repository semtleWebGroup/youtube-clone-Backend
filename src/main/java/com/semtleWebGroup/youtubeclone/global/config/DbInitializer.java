package com.semtleWebGroup.youtubeclone.global.config;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.VideoMedia;
import com.semtleWebGroup.youtubeclone.domain.video_media.repository.VideoMediaRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.repository.VideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;
import java.util.UUID;

/**
 * 나중에 없어지거나 변경될 수 있으므로 너무 많은 의존을 하지 마세요
 * 처음 애플리케이션 실행될 때 DB에 더미 데이터를 넣는 용도
 */
@Configuration
public class DbInitializer {

    public static UUID testVideoMediaId;
    public static UUID testVideoId;


    /**
     * 일단 목업이라도 동영상 스트리밍을 하려고 더미로 동영상 데이터를 넣음
     * @param channelRepository
     * @param videoRepository
     * @param videoMediaRepository
     * @return
     */
    @Bean
    public CommandLineRunner initDummyData(ChannelRepository channelRepository,
                                           VideoRepository videoRepository,
                                           VideoMediaRepository videoMediaRepository){
        //테스트용 비디오 넣기
        return (arg) -> {
            Channel testChannel = channelRepository.save(new Channel("영국남자", "영국 사람들"));
            Video testVideo = videoRepository.save(new Video(31, false, testChannel));
            VideoMedia testMedia = videoMediaRepository.save(VideoMedia.builder()
                    .rootVideo(testVideo)
                    .height(1080)
                    .width(1920)
                    .filePath(Paths.get("src/main/java/com/semtleWebGroup/youtubeclone/domain/video_media/storage/for_mock/file_example_MP4_1920_18MG.mp4").toAbsolutePath().toString())
                    .audioChannel(1)
                    .framePerSec(30)
                    .fileSize(18000)
                    .build()
            );
            testVideoMediaId = testMedia.getId();
            testVideoId = testVideo.getVideoId();
            System.out.println("test VideoMedia Dummy ID : " + testVideoMediaId);
            System.out.println("test Video Dummy ID : " + testVideoId);
        };

    }
}
