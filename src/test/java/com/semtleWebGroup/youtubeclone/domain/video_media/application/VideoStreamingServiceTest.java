package com.semtleWebGroup.youtubeclone.domain.video_media.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.VideoMedia;
import com.semtleWebGroup.youtubeclone.domain.video_media.exception.VideoFileNotExistException;
import com.semtleWebGroup.youtubeclone.domain.video_media.repository.VideoMediaRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import com.semtleWebGroup.youtubeclone.test_super.IntegrationTest;
import com.semtleWebGroup.youtubeclone.test_super.MockTest;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


//목업 테스트 , 목업으로 Repository 랑 최대 청크 사이즈 주입해줌
class VideoStreamingServiceTest extends MockTest {



    //constant
    private static final UUID testVideoId = UUID.randomUUID();
    private static final UUID invalidPathVideoId = UUID.randomUUID();
    private static final UUID invalidVideoId = UUID.randomUUID();
    private static final String sampleVideoPath = Paths.get("src/main/java/com/semtleWebGroup/youtubeclone/domain/video_media/storage/for_mock/file_example_MP4_1920_18MG.mp4").toAbsolutePath().toString();
    private static final String invalidVideoPath = Paths.get("src/main/java/com/semtleWebGroup/youtubeclone/domain/video_media/storage/for_mock/fake.mp4").toAbsolutePath().toString();
    private static final VideoMedia testVideoMedia = VideoMedia.builder()
            .width(1920)
            .height(1080)
            .rootVideo(new Video(30,false,new Channel("영국남자","문화 소개 채널")))
            .filePath(sampleVideoPath)
            .videoFormat("mp4")
            .audioChannel(2)
            .framePerSec(30)
            .fileSize(18000)
            .build();
    private static final VideoMedia invalidPathVideoMedia = VideoMedia.builder() //없는 파일
            .width(1920)
            .height(1080)
            .rootVideo(new Video(30,false,new Channel("영국남자","문화 소개 채널")))
            .filePath(invalidVideoPath)
            .videoFormat("mp4")
            .audioChannel(2)
            .framePerSec(30)
            .fileSize(18000)
            .build();


    //mock up
    private static VideoStreamingService videoStreamingService;
    private static VideoMediaRepository videoMediaRepository;

    @BeforeAll
    public static void setMockVideoMediaRepository(){
        videoMediaRepository=Mockito.mock(VideoMediaRepository.class);
        //정상적인 요청
        Mockito.when(videoMediaRepository.findById(testVideoId)).thenReturn(Optional.of(testVideoMedia));
        //존재하지 않는 파일 경로를 가지고 있는 DB 데이터
        Mockito.when(videoMediaRepository.findById(invalidPathVideoId)).thenReturn(Optional.of(invalidPathVideoMedia));
        //잘 못된 id로 요청
        Mockito.when(videoMediaRepository.findById(invalidVideoId)).thenReturn(Optional.empty());

        videoStreamingService = new VideoStreamingService(videoMediaRepository);
        ReflectionTestUtils.setField(videoStreamingService,"MAX_CHUNK_SIZE",1000); //임시로 최대 청크 사이즈 주입
    }


    @Nested
    @DisplayName("createResourceRegion 메서드")
    class createResourceRegion{

        @Test
        @DisplayName("정상적인 요청")
        public void validRequest() throws IOException {
            //given
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Range","bytes=0-10000000");
            List<HttpRange> range = httpHeaders.getRange();
            UUID videoId = testVideoId;


            //when
            List<ResourceRegion> resourceRegion = videoStreamingService.createResourceRegion(range, testVideoId);

            //then
            assertFalse(resourceRegion.isEmpty());
            Resource resource = resourceRegion.get(0).getResource();
            assertTrue(resource.exists());
            assertEquals(resource.getURI().getPath(), sampleVideoPath);


        }

        @Test
        @DisplayName("최대 사이즈 넘어서는 요청은 최대사이즈로")
        public void maxChunkSize() throws IOException {
            //given
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Range","bytes=0-" + (videoStreamingService.MAX_CHUNK_SIZE +999));
            List<HttpRange> range = httpHeaders.getRange();

            //when
            List<ResourceRegion> resourceRegion = videoStreamingService.createResourceRegion(range, testVideoId);

            //then
            assertFalse(resourceRegion.isEmpty());

            assertEquals(videoStreamingService.MAX_CHUNK_SIZE,resourceRegion.get(0).getCount());

        }
        @Test
        @DisplayName("DB 에는 존재하나 로컬 파일이 존재하지 않는 경우")
        public void withFileNotExist(){
            //given
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Range","bytes=0-10000");
            List<HttpRange> range = httpHeaders.getRange();

            //then throw
            assertThrows(VideoFileNotExistException.class,()->videoStreamingService.createResourceRegion(range,invalidPathVideoId));
        }

        @Test
        @DisplayName("아예 존재하지 않은 videoId 로 요청")
        public void invalidVideoId(){
            //given
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Range","bytes=0-10000");
            List<HttpRange> range = httpHeaders.getRange();

            //then throw
            assertThrows(BadRequestException.class,()->videoStreamingService.createResourceRegion(range,invalidVideoId));

        }


    }

}