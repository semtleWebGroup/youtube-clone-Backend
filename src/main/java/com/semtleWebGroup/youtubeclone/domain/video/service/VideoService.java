package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoInfoRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video_media.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import com.semtleWebGroup.youtubeclone.global.error.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.util.Optional;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private VideoInfoRepository videoInfoRepository;
    @Autowired
    private VideoRepository videoRepository;

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

    private VideoInfo getVideoInfoByVideoId(UUID videoId) {
        VideoInfo videoInfo = videoInfoRepository.findByVideo_VideoId(videoId)
            .orElseThrow(()-> new EntityNotFoundException("Video is not found."));
        return videoInfo;
    }

    @Transactional
    public VideoInfo edit(UUID videoId, VideoRequest dto) {
        VideoInfo video = this.getVideoInfoByVideoId(videoId);
        video.update(dto.getTitle(), dto.getDescription());
        videoInfoRepository.save(video);
        return video;
    }

    @Transactional
    public VideoInfo view(UUID videoId) {
        VideoInfo videoInfo = this.getVideoInfoByVideoId(videoId);
        videoInfo.incrementViewCount();
        videoInfoRepository.save(videoInfo);
        return videoInfo;
    }
}
