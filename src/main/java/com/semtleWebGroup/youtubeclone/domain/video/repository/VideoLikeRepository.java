package com.semtleWebGroup.youtubeclone.domain.video.repository;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {
    VideoLike deleteByVideoAndChannel(Video video, Channel channel);
}
