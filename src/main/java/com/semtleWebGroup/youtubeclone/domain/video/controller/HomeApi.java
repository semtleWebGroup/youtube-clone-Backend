package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoListResponse;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeApi {
    private final VideoService videoService;

    @GetMapping("")
    public ResponseEntity getList() {
        // TODO
        ArrayList<VideoListResponse> videos = new ArrayList<VideoListResponse>();
        for (int i = 0; i < 20; i++) {
            videos.add(
                    VideoListResponse.builder()
                            .title("title" + i)
                            .channelName("channel" + i)
                            .videoSec(i)
                            .viewCount(i)
                            .build()
            );
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videos);
    }
}
