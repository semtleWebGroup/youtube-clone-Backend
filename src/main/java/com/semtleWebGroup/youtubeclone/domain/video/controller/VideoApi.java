package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoViewDto;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoApi {
    private final VideoService videoService;

    @PostMapping("")
    public ResponseEntity create(@Valid @RequestBody VideoRequest videoRequest) throws Exception {
        // TODO: add된 video info 반환.
        Video video = Video.builder()
                .title(videoRequest.getTitle())
                .description(videoRequest.getDescription()).build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(video);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity view(@PathVariable Long videoId) {
        // TODO: 썸네일 없는 video info 반환. + 조회수 증가 필요
        List<String> qualityList = new ArrayList<String>();
        qualityList.add("p1080");
        qualityList.add("p480");
        VideoViewDto video = VideoViewDto.builder()
                .videoId(videoId)
                .channelId(1L)
                .channelName("ExampleChannel")
                .title("ExampleTitle")
                .description("ExampleDescription")
                .qualityList(qualityList).build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(video);
    }

    @PatchMapping("/{videoId}")
    public ResponseEntity update(@PathVariable Long videoId, @Valid @RequestBody VideoRequest videoRequest) {
        // TODO: title, description만 변경하여 변경 전의 video info 반환.
        Video video = Video.builder()
                .id(videoId)
                .title(videoRequest.getTitle())
                .description(videoRequest.getDescription()).build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(video);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(@PathVariable Long videoId) {
        // TODO: video media도 삭제하여 삭제 된 video info 반환.
        Video video = Video.builder()
                .id(videoId)
                .title("ExampleTitle")
                .description("ExampleDescription").build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(video);
    }

    @PostMapping("/{videoId}/like")
    public ResponseEntity like(@PathVariable Long videoId) {
        // TODO: like table에 등록 후 like 수 반환.
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("{ %d Like }", videoId));
    }

    @DeleteMapping("/{videoId}/like")
    public ResponseEntity dislike(@PathVariable Long videoId) {
        // TODO: like table에서 삭제 후 dislike 수 반환.
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("{ %d Dislike }", videoId));
    }

}