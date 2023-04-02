package com.semtleWebGroup.youtubeclone.domain.video.repository;

import com.semtleWebGroup.youtubeclone.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}