package com.semtleWebGroup.youtubeclone.domain.video.repository;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;


public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {

    VideoLike findByVideoAndChannel(Video video, Channel channel);

    @Modifying
    @Query(value = "DELETE FROM video WHERE video_id=:videoId and channel_id=:channelId", nativeQuery = true)
    void deleteByVideoAndChannel(UUID videoId, Long channelId);
}
