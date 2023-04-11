package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoInfoRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VideoService {
    @Autowired
    private VideoInfoRepository videoInfoRepository;

    public void create(VideoInfo video) throws IOException {
        videoInfoRepository.save(video);
    }

    public VideoInfo get(Integer id) {
        VideoInfo videoInfo = videoInfoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        String.format("%d is not found.", id)
                ));
        return videoInfo;
    }

    public VideoInfo edit(VideoInfo video, String title, String description, Byte[] thumbImg) {
        return video;
    }
}
