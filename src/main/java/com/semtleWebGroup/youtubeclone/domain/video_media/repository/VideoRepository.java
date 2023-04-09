package com.semtleWebGroup.youtubeclone.domain.video_media.repository;

import com.semtleWebGroup.youtubeclone.domain.video_media.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
}
