package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.entity.Video;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoApi {
    private final VideoService videoService;

    @PostMapping("")
    public ResponseEntity create(@ModelAttribute VideoRequest videoRequest) throws Exception {
//        Video video = videoService.add(videoRequest);
        String imagePath = System.getProperty("user.dir") + "/src/main/java/come/semtleWebGroup/youtubeclone/domain/video/storage/temp_image.jpg";
        Video video = Video.builder()
                .title("ExampleTitle")
                .description("ExampleDescription").build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(video);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity view(@PathVariable Long videoId) {
//        Video video = videoService.get(videoId);
        String imagePath = System.getProperty("user.dir") + "/src/main/java/come/semtleWebGroup/youtubeclone/domain/video/storage/temp_image.jpg";
        Video video = Video.builder()
                .title("ExampleTitle")
                .description("ExampleDescription").build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(video);
    }

    @PatchMapping("/{videoId}")
    public String update() {
        return "aa";
    }

    @DeleteMapping("/{videoId}")
    public String delete() {
        return "aa";
    }

    @PostMapping("/{videoId}/like")
    public String like() {
        return "aa";
    }

}