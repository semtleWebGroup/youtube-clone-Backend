package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.*;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import com.semtleWebGroup.youtubeclone.global.error.FieldError;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import com.semtleWebGroup.youtubeclone.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoApi {
    @Autowired
    private final VideoService videoService;

    @PostMapping("/{videoId}")
    public ResponseEntity create(
            @PathVariable UUID videoId,
            @RequestPart VideoRequest dto,
            @RequestPart("thumbImg") MultipartFile file
    ) {
        // TODO: convert thumbImg file type (to Blob)
        Blob blobImg = null;
        VideoInfo videoInfo;

        try {
            videoInfo = videoService.add(videoId, dto, blobImg);
        } catch (EntityNotFoundException e) {
            throw new BadRequestException(FieldError.of("videoId", String.valueOf(videoId), e.getMessage()));
        } catch (InvalidValueException e) {
            throw new BadRequestException(FieldError.of("videoId", String.valueOf(videoId), e.getMessage()));
        }

        VideoResponse videoResponse = new VideoResponse(videoInfo);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(videoResponse);
    }

    @GetMapping("/{videoId}")
    @Transactional
    public ResponseEntity view(@PathVariable UUID videoId) {
        VideoInfo videoInfo;

        try {
            videoInfo = videoService.view(videoId);
        } catch (EntityNotFoundException e) {
            throw new BadRequestException(FieldError.of("videoId", String.valueOf(videoId), e.getMessage()));
        } catch (InvalidValueException e) {
            throw new BadRequestException(FieldError.of("videoId", String.valueOf(videoId), e.getMessage()));
        }

        Video video = videoInfo.getVideo();
        Channel channel = video.getChannel();
        ArrayList<String> qualityList = new ArrayList<String>();
        qualityList.add("1080p");
        qualityList.add("360p");
        VideoViewResponse videoViewResponse = VideoViewResponse.builder()
                .videoId(video.getVideoId())
                .channelId(channel.getId())
                .channelName(channel.getTitle())
                .channelProfileImg(null) // TODO: channel image가 blob으로 돼야 함.
                .channelSubscriberCount(0) // TODO: channel에서 subscriber count를 제공해주는 메서드가 있었으면 좋겠음.
                .title(videoInfo.getTitle())
                .description(videoInfo.getDescription())
                .createdTime(videoInfo.getCreatedTime())
                .videoSec(video.getVideoSecond())
                .viewCount(videoInfo.getViewCount())
                .likeCount(0) // TODO: video에서 like 수를 반환하는 메서드가 있어야 함.
                .isLike(false) // TODO: 토큰을 확인해서 해당 채널-비디오 id가 맞는 like가 있는지 확인해야 함. like/dislike 기능 구현되면 수정할 예정.
                .qualityList(qualityList) // TODO: Video_media 테이블 확인하여 사용 가능한 화질 리스트 가져와야 함. 이건 화질이 어느 column인지 몰라서 구현하지 않음.
                .build();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoViewResponse);
    }

    @PatchMapping("/{videoId}")
    public ResponseEntity update(
            @PathVariable UUID videoId,
            @Valid @RequestBody VideoRequest dto
    ) {
        VideoInfo videoInfo;

        try {
            videoInfo = videoService.edit(videoId, dto);
        } catch (EntityNotFoundException e) {
            throw new BadRequestException(FieldError.of("videoId", String.valueOf(videoId), e.getMessage()));
        }

        VideoResponse videoResponse = new VideoResponse(videoInfo);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoResponse);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(@PathVariable UUID videoId) {
        VideoInfo videoInfo;

        try {
            videoInfo = videoService.delete(videoId);
        } catch (EntityNotFoundException e) {
            throw new BadRequestException(FieldError.of("videoId", String.valueOf(videoId), e.getMessage()));
        }

        VideoResponse videoResponse = new VideoResponse(videoInfo);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(videoResponse);
    }

    @PostMapping("/{videoId}/like")
    public ResponseEntity like(@PathVariable UUID videoId) {
        // TODO: like table에 등록 후 like 수 반환.
        VideoLikeResponse like = new VideoLikeResponse(true, 1);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(like);
    }

    @DeleteMapping("/{videoId}/like")
    public ResponseEntity dislike(@PathVariable UUID videoId) {
        // TODO: like table에서 삭제 후 like 수 반환.
        VideoLikeResponse like = new VideoLikeResponse(false, 0);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(like);
    }

}