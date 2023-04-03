package com.semtleWebGroup.youtubeclone.domain.video.repository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByTitleContaining(String key);

}