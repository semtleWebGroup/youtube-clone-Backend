package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public void create(Video video) throws IOException {
        videoRepository.save(video);
    }

    public Video get(Integer id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        String.format("%d is not found.", id)
                ));
        return video;
    }

    public Video edit(Video video, String title, String description, Byte[] thumbImg) {
        return video;
    }
}
