package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoUpdateDto;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoViewDto;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoApi {
    @Autowired
    private final VideoService videoService;

    @Transactional
    @PostMapping("/{videoId}")
    public ResponseEntity create(
            @PathVariable Long videoId,
            @RequestPart VideoRequest dto,
            @RequestPart("thumbImg") MultipartFile file
    ) throws Exception
    {
        // TODO: convert thumbImg file type (to Blob)
        Blob blobImg = null;

        // save and return video
        VideoInfo video = videoService.add(videoId, dto, blobImg);
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
    public ResponseEntity update(
            @PathVariable Long videoId,
            @Valid @RequestBody VideoRequest dto
    ) {
        VideoInfo video = videoService.edit(videoId, dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(video);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(@PathVariable Long videoId) {
        // TODO: video media도 삭제하여 삭제 된 video info 반환.
        VideoUpdateDto video = VideoUpdateDto.builder()
                .videoId(videoId)
                .title("ExampleTitle")
                .description("ExampleDescription").build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(video);
    }

    @PostMapping("/{videoId}/like")
    public ResponseEntity like(@PathVariable Long videoId) {
        // TODO: like table에 등록 후 like 수 반환.
        VideoLikeResponse like = new VideoLikeResponse(true, 1);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(like);
    }

    @DeleteMapping("/{videoId}/like")
    public ResponseEntity dislike(@PathVariable Long videoId) {
        // TODO: like table에서 삭제 후 like 수 반환.
        VideoLikeResponse like = new VideoLikeResponse(false, 0);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(like);
    }

}