package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.entity.Video;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class VideoService {
    private VideoRepository videoRepository;

    public Video add(VideoRequest dto) {
        return new Video();
    }

    public Video get(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException());
        return video;
    }
}
