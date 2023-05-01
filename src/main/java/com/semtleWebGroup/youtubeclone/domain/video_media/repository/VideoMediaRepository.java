package com.semtleWebGroup.youtubeclone.domain.video_media.repository;

import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video_media.domain.VideoMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VideoMediaRepository extends JpaRepository<VideoMedia, UUID> {
    VideoMedia findByRootVideo(Video video);
}
