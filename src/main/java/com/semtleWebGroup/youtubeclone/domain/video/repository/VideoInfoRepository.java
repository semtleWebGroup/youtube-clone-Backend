package com.semtleWebGroup.youtubeclone.domain.video.repository;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoInfoRepository extends JpaRepository<VideoInfo, Long> {
    List<VideoInfo> findByTitleContaining(String key);
    Optional<VideoInfo> findByVideo(Video video);
    Optional<VideoInfo> findByVideo_VideoId(UUID videoId);
}