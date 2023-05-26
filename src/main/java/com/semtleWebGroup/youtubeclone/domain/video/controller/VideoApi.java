package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.auth.dto.TokenInfo;
import com.semtleWebGroup.youtubeclone.domain.channel.application.ChannelService;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.dto.*;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import com.semtleWebGroup.youtubeclone.global.error.FieldError;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoApi {
    private final VideoService videoService;
    private final ChannelService channelService; // token에서 channel 가져오는 용도

    private void checkImgFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        String[] validExtensions = {"jpg", "png"};

        for (String validExtension: validExtensions) {
            if (extension.equals(validExtension)) return;
        }
        throw new BadRequestException(FieldError.of("thumbImg", fileName, "Only jpg and png are available."));
    }

    private void checkVideoFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        String[] validExtensions = {"mp4", "avi"};

        for (String validExtension: validExtensions) {
            if (extension.equalsIgnoreCase(validExtension)) return;
        }
        throw new BadRequestException(FieldError.of("videoFile", fileName, "Only mp4 and avi are available."));
    }

    @PostMapping("")
    public ResponseEntity upload(
            @RequestPart MultipartFile videoFile,
            @RequestPart(required = false) MultipartFile thumbImg,
            @AuthenticationPrincipal TokenInfo principal
            )
    {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);

        if (!thumbImg.isEmpty()) this.checkImgFileExtension(thumbImg);
        this.checkVideoFileExtension(videoFile);

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
        @RequestBody @Valid VideoRequest data,
        @AuthenticationPrincipal TokenInfo principal
    ) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);

        VideoEditDto dto = VideoEditDto.builder()
            .channel(channel)
            .videoId(videoId)
            .title(data.getTitle())
            .description(data.getDescription())
            .build();
        VideoResponse videoResponse = videoService.edit(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(videoResponse);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity view(
            @PathVariable UUID videoId,
            @AuthenticationPrincipal TokenInfo principal
    ) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);

        VideoViewResponse videoViewResponse = videoService.view(videoId, channel);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoViewResponse);
    }

    @PatchMapping("/{videoId}")
    public ResponseEntity update(
        @PathVariable UUID videoId,
        @RequestBody @Valid VideoRequest data,
        @AuthenticationPrincipal TokenInfo principal
    ) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);

        VideoEditDto dto = VideoEditDto.builder()
            .channel(channel)
            .videoId(videoId)
            .title(data.getTitle())
            .description(data.getDescription())
            .build();
        VideoResponse videoResponse = videoService.edit(dto);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoResponse);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(
            @PathVariable UUID videoId,
            @AuthenticationPrincipal TokenInfo principal
    ) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);

        VideoResponse videoResponse = videoService.delete(videoId, channel);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoResponse);
    }

    @PostMapping("/{videoId}/like")
    public ResponseEntity like(
            @PathVariable UUID videoId,
            @AuthenticationPrincipal TokenInfo principal
    ) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);

        VideoLikeResponse videoLikeResponse = videoService.like(videoId, channel);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videoLikeResponse);
    }

    @DeleteMapping("/{videoId}/like")
    public ResponseEntity dislike(
            @PathVariable UUID videoId,
            @AuthenticationPrincipal TokenInfo principal
    ) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);

        VideoLikeResponse videoLikeResponse = videoService.dislike(videoId, channel);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videoLikeResponse);
    }

}