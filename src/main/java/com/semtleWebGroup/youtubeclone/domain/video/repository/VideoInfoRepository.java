package com.semtleWebGroup.youtubeclone.domain.video.repository;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoInfoRepository extends JpaRepository<VideoInfo, Long> {
    List<VideoInfo> findByTitleContaining(String key);



}