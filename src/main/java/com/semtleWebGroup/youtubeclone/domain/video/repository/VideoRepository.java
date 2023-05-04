package com.semtleWebGroup.youtubeclone.domain.video.repository;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
    List<Video> findByTitleContaining(String key);
}