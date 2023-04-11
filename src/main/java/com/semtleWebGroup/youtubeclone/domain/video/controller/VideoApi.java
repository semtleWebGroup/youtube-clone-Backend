package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.channel.application.ChannelService;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoUpdateDto;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoViewDto;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
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
    ) throws Exception
    {
        // TODO: convert thumbImg file type (to Blob)
        Blob blobImg = null;

        // save and return video
//        VideoInfo video = videoService.add(videoId, dto, blobImg);
        return ResponseEntity
                .status(HttpStatus.CREATED)
//                .body(video);
                .body(videoId);
    }

    @GetMapping("/{videoId}")
    @Transactional
    public ResponseEntity view(@PathVariable UUID videoId) throws Exception {
        // TODO: 썸네일 없는 video info 반환. + 조회수 증가 필요
        VideoInfo videoInfo = videoService.getVideoInfoByVideoId(videoId);
        Video video = videoInfo.getVideo();
        Channel channel = video.getChannel();
        ArrayList<String> qualityList = new ArrayList<String>();
        qualityList.add("1080p");
        qualityList.add("360p");
        VideoViewDto videoViewDto = VideoViewDto.builder()
                .videoId(video.getVideoId())
                .channelId(channel.getId())
                .channelName(channel.getTitle())
                .channelProfileImg(null) // TODO: channel image가 blob으로 돼야 함.
                .channelSubscriberCount(0) // TODO: channel에서 subscriber count를 제공해주는 메서드가 있었으면 좋겠음.
                .title(videoInfo.getTitle())
                .description(videoInfo.getDescription())
                .videoSec(video.getVideoSecond())
                .viewCount(videoInfo.getViewCount())
                .likeCount(0) // TODO: video에서 like 수를 반환하는 메서드가 있어야 함.
                .isLike(false) // TODO: 토큰을 확인해서 해당 채널-비디오 id가 맞는 like가 있는지 확인해야 함. like/dislike 기능 구현되면 수정할 예정.
                .qualityList(qualityList) // TODO: Video_media 테이블 확인하여 사용 가능한 화질 리스트 가져와야 함. 이건 화질이 어느 column인지 몰라서 구현하지 않음.
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videoViewDto);
    }

    @PatchMapping("/{videoId}")
    public ResponseEntity update(
            @PathVariable UUID videoId,
            @Valid @RequestBody VideoRequest dto
    ) {
        VideoInfo video = videoService.edit(videoId, dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(video);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity delete(@PathVariable UUID videoId) {
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