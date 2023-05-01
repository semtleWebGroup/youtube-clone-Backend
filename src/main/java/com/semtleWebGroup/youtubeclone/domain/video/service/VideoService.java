package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.exception.VideoInfoExistException;
import com.semtleWebGroup.youtubeclone.domain.video.exception.VideoNotCachedException;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoInfoRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.application.VideoMediaService;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.VideoMedia;
import com.semtleWebGroup.youtubeclone.domain.video_media.repository.VideoMediaRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.repository.VideoRepository;
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
    private final VideoLikeService videoLikeService;
    private final VideoMediaService videoMediaService;
    private final VideoMediaRepository videoMediaRepository;

    private VideoInfo getVideoInfoByVideoId(UUID videoId) {
        VideoInfo videoInfo = videoInfoRepository.findByVideo_VideoId(videoId)
                .orElseThrow(()-> new EntityNotFoundException("Video Info is not found."));
        return videoInfo;
    }

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

    @Transactional
    public VideoInfo view(UUID videoId) {
        VideoInfo videoInfo = this.getVideoInfoByVideoId(videoId);

        // Video.isCached가 False인 경우 error
        if (!videoInfo.getVideo().isCashed()) throw new VideoNotCachedException("Video is caching.");

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
        VideoInfo videoInfo = this.getVideoInfoByVideoId(videoId);
        videoInfoRepository.delete(videoInfo);

//        videoMediaService.delete(videoId);
        Optional<Video> video = videoRepository.findById(videoId);
        VideoMedia videoMedia = videoMediaRepository.findByRootVideo(video.get());
        videoMediaRepository.delete(videoMedia);

//        videoLikeService.delete(videoId);

        videoRepository.deleteById(videoId);
        return videoInfo;
    }
}
