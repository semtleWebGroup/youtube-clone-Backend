package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoApi {
    @Autowired
    private final VideoService videoService;

    @PostMapping(
            path = "/{videoId}",
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity create(
            @PathVariable Integer videoId,
            @RequestPart VideoRequest dto,
            @RequestPart("thumbImg") MultipartFile file
        ) throws Exception
    {
        Video video = videoService.add(videoId, dto, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(video);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity view(@PathVariable Integer videoId) {
        Video video = videoService.addViewCount(videoId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(video);
    }

    @PatchMapping("/{videoId}")
    public ResponseEntity update(
            @PathVariable Integer videoId,
            @Valid @RequestBody VideoRequest dto
    ) {
        Video video = videoService.edit(videoId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(video);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(@PathVariable Integer videoId) {
        // TODO: video 및 video media도 삭제하여 삭제 된 video info 반환.
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }

    @PostMapping("/{videoId}/like")
    public ResponseEntity like(@PathVariable Integer videoId) {
        // TODO: like table에 등록 후 like 수 반환.
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("{ %d Like }", videoId));
    }

    @DeleteMapping("/{videoId}/like")
    public ResponseEntity dislike(@PathVariable Integer videoId) {
        // TODO: like table에서 삭제 후 dislike 수 반환.
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("{ %d Dislike }", videoId));
    }

}