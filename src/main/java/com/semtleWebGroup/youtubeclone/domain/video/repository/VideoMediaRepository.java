package com.semtleWebGroup.youtubeclone.domain.video.repository;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoMediaRepository extends JpaRepository<VideoMedia, Integer> {
}