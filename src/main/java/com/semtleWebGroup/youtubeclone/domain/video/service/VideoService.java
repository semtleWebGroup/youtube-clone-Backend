package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoInfoRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video_media.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import com.semtleWebGroup.youtubeclone.global.error.exception.InvalidValueException;
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

    private VideoInfo getVideoInfoByVideoId(UUID videoId) {
        VideoInfo videoInfo = videoInfoRepository.findByVideo_VideoId(videoId)
                .orElseThrow(()-> new EntityNotFoundException("Video is not found."));
        return videoInfo;
    }

    @Transactional
    public VideoInfo add(UUID videoId, VideoRequest dto, Blob thumbImg) {
        Video video = videoRepository.findById(videoId)
            .orElseThrow(()-> new EntityNotFoundException("Video is not found."));

        Optional<VideoInfo> videoInfo = videoInfoRepository.findByVideo(video);
        if (videoInfo != null)
            throw new InvalidValueException("Video info of video is already exist.");

        VideoInfo newVideoInfo = VideoInfo.builder()
            .video(video)
            .title(dto.getTitle())
            .description(dto.getDescription())
            .thumbImg(thumbImg)
            .build();
        videoInfoRepository.save(newVideoInfo);
        return newVideoInfo;
    }

    @Transactional
    public VideoInfo view(UUID videoId) {
        VideoInfo videoInfo = this.getVideoInfoByVideoId(videoId);
        if (videoInfo.getVideo().isCashed()) {
            throw new InvalidValueException("Video encoding is not finished.");
        }
        videoInfo.incrementViewCount();
        videoInfoRepository.save(videoInfo);
        return videoInfo;
    }

    @Transactional
    public VideoInfo edit(UUID videoId, VideoRequest dto) {
        VideoInfo videoInfo = this.getVideoInfoByVideoId(videoId);
        videoInfo.update(dto.getTitle(), dto.getDescription());
        videoInfoRepository.save(videoInfo);
        return videoInfo;
    }

    @Transactional
    public VideoInfo delete(UUID videoId) {
        // TODO: VideoMedia, Video 등 관련 모든 데이터 삭제 필요.
        VideoInfo videoInfo = this.getVideoInfoByVideoId(videoId);
        videoInfoRepository.deleteById(videoInfo.getId());
        return videoInfo;
    }


}
