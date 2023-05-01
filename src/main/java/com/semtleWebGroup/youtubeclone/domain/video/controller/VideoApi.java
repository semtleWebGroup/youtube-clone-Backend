package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.*;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoApi {
    private final VideoService videoService;

    @PostMapping("/{videoId}")
    public ResponseEntity create(
            @PathVariable UUID videoId,
            @RequestPart VideoRequest dto,
            @RequestPart("thumbImg") MultipartFile thumbImg
    ) throws IOException, SQLException {
        Blob blobImg = new SerialBlob(thumbImg.getBytes());
        VideoInfo videoInfo = videoService.add(videoId, dto, blobImg);
        VideoResponse videoResponse = new VideoResponse(videoInfo);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(videoResponse);
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
        VideoUpdateDto video = VideoUpdateDto.builder()
                .videoId(videoId)
                .title(videoRequest.getTitle())
                .description(videoRequest.getDescription()).build();

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