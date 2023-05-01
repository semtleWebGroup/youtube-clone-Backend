package com.semtleWebGroup.youtubeclone.domain.video.repository;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VideoLikeRepository extends JpaRepository<VideoLike, UUID> {

}
