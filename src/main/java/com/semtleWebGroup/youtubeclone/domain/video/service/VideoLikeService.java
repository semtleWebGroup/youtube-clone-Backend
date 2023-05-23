package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoLike;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoLikeService {
    private final VideoLikeRepository videoLikeRepository;
    private final VideoService videoService;

    public VideoLikeResponse get(Video video, Channel channel) {
        return VideoLikeResponse.builder()
                .videoId(video.getId())
                .likeCount(video.getLikeCount())
                .isLike(video.isLike(channel))
                .build();
    }

    @Transactional
    public VideoLikeResponse add(UUID videoId, Channel channel) {
        Video video = videoService.getVideo(videoId);
        VideoLike videoLike = VideoLike.builder()
                .channel(channel)
                .build();

        channel.likeVideo(video);   // channel에 좋아요한 video 추가
        video.likeVideo(videoLike); // video에 like 추가 & like에 video 추가
        videoLikeRepository.save(videoLike);
        return this.get(video, channel);
    }

    @Transactional
    public VideoLikeResponse delete(UUID videoId, Channel channel) {
        Video video = videoService.getVideo(videoId);
        VideoLike videoLike = videoLikeRepository.findByVideoAndChannel(video, channel);
        video.unLikeVideo(videoLike);   // video에 like 제거
        channel.unLikeVideo(video);     // channel에 좋아요한 video 제거
        videoLikeRepository.deleteByVideoAndChannel(video, channel);
        return this.get(video, channel);
    }
}
