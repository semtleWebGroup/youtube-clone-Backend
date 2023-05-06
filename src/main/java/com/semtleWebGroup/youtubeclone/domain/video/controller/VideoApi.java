package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.dto.*;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoLikeService;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import com.semtleWebGroup.youtubeclone.global.error.exception.MediaServerException;
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

    @PostMapping("")
    public ResponseEntity upload(
            @RequestPart MultipartFile videoFile,
            @RequestPart(required = false) MultipartFile thumbImg,
            @RequestPart Channel channel
    ) throws MediaServerException {
        VideoUploadDto dto = VideoUploadDto.builder()
                .channel(channel)
                .videoFile(videoFile)
                .thumbImg(thumbImg)
                .build();
        VideoResponse videoResponse = videoService.upload(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(videoResponse);
    }

    @PostMapping("/{videoId}")
    public ResponseEntity create(
        @PathVariable UUID videoId,
        @RequestPart @Valid VideoRequest data,
        @RequestPart(required=false) MultipartFile thumbImg,
        @RequestPart Channel channel
    ) throws IOException, SQLException {
        Blob blobImg = (thumbImg == null) ? null : new SerialBlob(thumbImg.getBytes());
        VideoEditDto dto = VideoEditDto.builder()
            .channel(channel)
            .videoId(videoId)
            .title(data.getTitle())
            .description(data.getDescription())
            .thumbImg(blobImg)
            .build();
        VideoResponse videoResponse = videoService.edit(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(videoResponse);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity view(@PathVariable UUID videoId, @RequestPart Channel channel) {
        VideoViewResponse videoViewResponse = videoService.view(videoId, channel);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoViewResponse);
    }

    @PatchMapping("/{videoId}")
    public ResponseEntity update(
        @PathVariable UUID videoId,
        @RequestPart @Valid VideoRequest data,
        @RequestPart(required=false) MultipartFile thumbImg,
        @RequestPart Channel channel
    ) throws IOException, SQLException {
        Blob blobImg = (thumbImg == null) ? null : new SerialBlob(thumbImg.getBytes());
        VideoEditDto dto = VideoEditDto.builder()
            .channel(channel)
            .videoId(videoId)
            .title(data.getTitle())
            .description(data.getDescription())
            .thumbImg(blobImg)
            .build();
        VideoResponse videoResponse = videoService.edit(dto);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoResponse);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(@PathVariable UUID videoId, @RequestPart Channel channel) {
        VideoResponse videoResponse = videoService.delete(videoId, channel);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoResponse);
    }

    @PostMapping("/{videoId}/like")
    public ResponseEntity like(@PathVariable UUID videoId, @RequestPart Channel channel) { // TODO: channel 로그인 정보에서 가져오기
        VideoLikeResponse videoLikeResponse = videoLikeService.add(videoId, channel);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videoLikeResponse);
    }

    @DeleteMapping("/{videoId}/like")
    public ResponseEntity dislike(@PathVariable UUID videoId, @RequestPart Channel channel) { // TODO: channel 로그인 정보에서 가져오기
        VideoLikeResponse videoLikeResponse = videoLikeService.delete(videoId, channel);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videoLikeResponse);
    }

}