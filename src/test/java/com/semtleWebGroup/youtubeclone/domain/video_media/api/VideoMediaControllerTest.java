package com.semtleWebGroup.youtubeclone.domain.video_media.api;

import com.semtleWebGroup.youtubeclone.domain.video_media.application.VideoMediaService;
import com.semtleWebGroup.youtubeclone.domain.video_media.application.VideoStreamingService;
import com.semtleWebGroup.youtubeclone.domain.video_media.exception.VideoFileNotExistException;
import com.semtleWebGroup.youtubeclone.domain.video_media.util.ResourceRegionFactory;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = {VideoMediaController.class})
@ActiveProfiles("test")
class VideoMediaControllerTest{

    final static UUID testMediaId = UUID.randomUUID();
    final static UUID invalidId = UUID.randomUUID();
    final static UUID noFileID = UUID.randomUUID();


    @Autowired
    MockMvc mockMvc;


    @MockBean
    VideoStreamingService videoStreamingService;

    @MockBean
    VideoMediaService videoMediaService;


    @Nested
    @DisplayName("[API][GET] /medias/{mediaId}")
    class videoStreaming{

        List<HttpRange> range;



        @BeforeEach
        public void setService() throws IOException {
            //range
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Range","bytes=0-10000");
            range = httpHeaders.getRange();

            FileSystemResource fileSystemResource = new FileSystemResource(Paths.get("src/main/java/com/semtleWebGroup/youtubeclone/domain/video_media/storage/for_mock/file_example_MP4_1920_18MG.mp4"));

            //List<ResourceRegion>
            List<ResourceRegion> resourceRegions = ResourceRegionFactory.fromRanges(range,
                    fileSystemResource,
                    1000);

            when(videoStreamingService.createResourceRegion(range, testMediaId)).thenReturn(resourceRegions);
            when(videoStreamingService.createResourceRegion(range,invalidId)).thenThrow(new BadRequestException(Collections.emptyList()));
            when(videoStreamingService.createResourceRegion(range,noFileID)).thenThrow(new VideoFileNotExistException(fileSystemResource));

        }

        @Test
        @DisplayName("정상적인 요청")
        public void withValidRequest() throws Exception {

            mockMvc.perform(get("/medias/"+ testMediaId).header("Range","bytes=0-10000"))
                    .andExpect(status().isPartialContent())
                    .andExpect(header().string("Content-Type",anyOf(
                            is(MediaType.APPLICATION_OCTET_STREAM_VALUE),
                            Matchers.startsWith("video/")
                    )))
                    .andExpect(header().string("Content-Range",Matchers.startsWith("bytes")));
        }

        @Test
        @DisplayName("Range 헤더를 보내지 않을때")
        public void withNoRange() throws Exception {

            mockMvc.perform(get("/medias/"+ testMediaId))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Range 내용 prefix 오류시")
        public void withInvalidPrefix() throws Exception {

            mockMvc.perform(get("/medias/"+ testMediaId).header("Range","byte=0-10000"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Range + 빈 범위")
        public void withEmptyRange() throws Exception {

            mockMvc.perform(get("/medias/"+ testMediaId).header("Range",""))
                    .andExpect(status().isBadRequest());
        }
        @Test
        @DisplayName("잘못된 UUID 요청")
        public void invalidUUID() throws Exception {

            mockMvc.perform(get("/medias/"+ invalidId).header("Range","bytes=0-10000"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("서버 내부적으로 로컬파일 실종")
        public void internalFileMissing() throws Exception {

            mockMvc.perform(get("/medias/"+ noFileID).header("Range","bytes=0-10000"))
                    .andExpect(status().isNotFound());
        }

    }


}