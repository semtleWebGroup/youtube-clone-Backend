package com.semtleWebGroup.youtubeclone.domain.video.repository;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
    List<Video> findByTitleContaining(String key);
    Page<Video> findAllByOrderByCreatedTimeDesc(Pageable pageable);

    Page<Video> findByStatusOrderByCreatedTimeDesc(Pageable pageable, Video.VideoStatus status);
    Page<Video> searchVideosByTitleContainingOrderByCreatedTimeDesc(String keyword, Pageable pageable);
}