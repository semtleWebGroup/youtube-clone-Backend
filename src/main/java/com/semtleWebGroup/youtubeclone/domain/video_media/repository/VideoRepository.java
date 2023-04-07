package com.semtleWebGroup.youtubeclone.domain.video_media.repository;

import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
}
