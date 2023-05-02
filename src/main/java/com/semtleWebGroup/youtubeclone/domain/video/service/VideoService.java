package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoViewResponse;
import com.semtleWebGroup.youtubeclone.domain.video.exception.VideoNotCachedException;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoLikeService videoLikeService;
    private final MediaServerSpokesman mediaServerSpokesman;

    private Video getVideoInfoByVideoId(UUID videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(()-> new EntityNotFoundException("Video is not found."));
        return video;
    }

    @Transactional
    public Video add(UUID videoId, VideoRequest dto, Blob thumbImg) {
        // Video가 없는 경우 error
        Video video = getVideoInfoByVideoId(videoId);
        // Video가 아직 캐싱되지 않았다면 error
        if (video.getStatus() != MediaServerSpokesman.EncodingStatus.FINISHED) throw new VideoNotCachedException("Video is caching.");

        video.update(dto.getTitle(), dto.getDescription(), thumbImg);
        videoRepository.save(video);
        return video;
    }

    @Transactional
    public VideoViewResponse view(UUID videoId) {
        Video video = this.getVideoInfoByVideoId(videoId);

        // Video가 아직 캐싱되지 않았다면 error
        if (video.getStatus() != MediaServerSpokesman.EncodingStatus.FINISHED)
            throw new VideoNotCachedException("Video is caching.");

        video.incrementViewCount();
        videoRepository.save(video);

        VideoViewResponse videoViewResponse = VideoViewResponse.builder()
            .video(video)
            .videoLike(videoLikeService.get(video.getVideoId()))
//                .qualityList(mediaServerSpokesman.getQualityList(videoInfo.getVideo().getVideoId())) // TODO
            .build();
        return videoViewResponse;
    }

    @Transactional
    public Video edit(UUID videoId, VideoRequest dto) {
        Video video = this.getVideoInfoByVideoId(videoId);
        video.update(dto.getTitle(), dto.getDescription());
        videoRepository.save(video);
        return video;
    }

    @Transactional
    public Video delete(UUID videoId) {
        Video videoInfo = this.getVideoInfoByVideoId(videoId);
        videoRepository.delete(videoInfo);

//        videoMediaService.delete(videoId);

//        videoLikeService.delete(videoId);

        videoRepository.deleteById(videoId);
        return videoInfo;
    }
}
