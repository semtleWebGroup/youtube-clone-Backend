package com.semtleWebGroup.youtubeclone.domain.video_media.aop;

import com.semtleWebGroup.youtubeclone.domain.video_media.dto.response.GetEncodingStatusResponse;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MockedMediaServerSpokesman;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
class MediaServerApiLoggerTest {
    @Autowired
    private MediaServerSpokesman mediaServerSpokesman;

    @Test
    void instanceTest() throws Throwable {
        // 프록시를 테스트 하는 방법을 모르겠음
        Assertions.assertInstanceOf(MockedMediaServerSpokesman.class, mediaServerSpokesman);
        GetEncodingStatusResponse.EncodingStatus encodingStatus = mediaServerSpokesman.getEncodingStatus(UUID.randomUUID());
        System.out.println(encodingStatus);
    }

}