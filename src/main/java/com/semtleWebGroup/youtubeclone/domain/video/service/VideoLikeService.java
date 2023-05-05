package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoLike;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoLikeRepository;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoLikeService {
    private final VideoLikeRepository videoLikeRepository;
    private final VideoService videoService;
    private final VideoRepository videoRepository;

    public VideoLikeResponse get(Video video, Channel channel) {
        return VideoLikeResponse.builder()
                .likeCount(video.getLikeCount())
                .isLike(video.isLike(channel))
                .build();
    }

    @Transactional
    public VideoLikeResponse add(UUID videoId, Channel channel) {
        Video video = videoService.getVideo(videoId);
        VideoLike videoLike = VideoLike.builder()
                .channel(channel)
                .video(video)
                .build();
        video.getLikedChannels().add(channel);
        videoRepository.save(video);
        videoLikeRepository.save(videoLike);
        return VideoLikeResponse.builder()
                .likeCount(video.getLikeCount())
                .isLike(video.isLike(channel))
                .build();
    }

    public VideoLikeResponse delete(UUID videoId, Channel channel) {
        Video video = videoService.getVideo(videoId);
        videoLikeRepository.deleteByVideoAndChannel(video, channel);
        return VideoLikeResponse.builder()
                .likeCount(video.getLikeCount())
                .isLike(video.isLike(channel))
                .build();
    }
}
