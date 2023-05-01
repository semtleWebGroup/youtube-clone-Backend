package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.exception.VideoInfoExistException;
import com.semtleWebGroup.youtubeclone.domain.video.exception.VideoNotCachedException;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoInfoRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video_media.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoInfoRepository videoInfoRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public VideoInfo add(UUID videoId, VideoRequest dto, Blob thumbImg) {
        // Video가 없는 경우 error
        Video video = videoRepository.findById(videoId).orElseThrow(()-> new EntityNotFoundException("Video is not found."));
        // Video.isCached가 False인 경우 error
        if (!video.isCashed()) throw new VideoNotCachedException("Video is caching.");
        // video에 대한 videoInfo가 이미 있는 경우 error
        VideoInfo videoInfo = videoInfoRepository.findByVideo(video);
        if (videoInfo != null) throw new VideoInfoExistException("Video info of video is already exist.");

        VideoInfo newVideoInfo = VideoInfo.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .thumbImg(thumbImg)
                .video(video)
                .build();
        videoInfoRepository.save(newVideoInfo);
        return newVideoInfo;
    }

    public VideoInfo get(UUID id) {
        VideoInfo videoInfo = videoInfoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        String.format("%d is not found.", id)
                ));
        return videoInfo;
    }
}
