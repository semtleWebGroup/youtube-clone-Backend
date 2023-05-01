package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.*;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoLikeService;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import com.semtleWebGroup.youtubeclone.domain.video_media.application.VideoMediaService;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoApi {
    private final VideoService videoService;
    private final VideoLikeService videoLikeService;

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
    public ResponseEntity view(@PathVariable UUID videoId) {
        VideoViewResponse videoViewResponse = videoService.view(videoId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoViewResponse);
    }

    @PatchMapping("/{videoId}")
    public ResponseEntity update(
            @PathVariable UUID videoId,
            @Valid @RequestBody VideoRequest dto
    ) {
        VideoInfo videoInfo = videoService.edit(videoId, dto);

        VideoResponse videoResponse = new VideoResponse(videoInfo);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoResponse);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(@PathVariable UUID videoId) {
        VideoInfo videoInfo = videoService.delete(videoId);

        VideoResponse videoResponse = new VideoResponse(videoInfo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videoResponse);
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