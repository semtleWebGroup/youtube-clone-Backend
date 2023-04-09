package com.semtleWebGroup.youtubeclone.domain.video_media.repository;

import com.semtleWebGroup.youtubeclone.domain.video_media.domain.VideoMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoMediaRepository extends JpaRepository<VideoMedia, UUID> {
}
