package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.VideoInfo;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoInfoRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Blob;

@Service
public class VideoService {
    @Autowired
    private VideoInfoRepository videoInfoRepository;

    @Transactional
    public VideoInfo add(Long id, VideoRequest dto, Blob thumbImg) {
        VideoInfo video = this.get(id);
        video.update(dto.getTitle(), dto.getDescription(), thumbImg);
        videoInfoRepository.save(video);
        return video;
    }

    public VideoInfo get(Long id) {
        VideoInfo videoInfo = videoInfoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        String.format("%d is not found.", id)
                ));
        return videoInfo;
    }

    @Transactional
    public VideoInfo edit(Long id, VideoRequest dto) {
        VideoInfo video = this.get(id);
        video.update(dto.getTitle(), dto.getDescription());
        videoInfoRepository.save(video);
        return video;
    }

    @Transactional
    public VideoInfo view(Long id) {
        // TODO: channel 관련 정보, media type list도 join해서 보내줄 것
        VideoInfo video = this.get(id);
        video.incrementViewCount();
        videoInfoRepository.save(video);
        return video;
    }
}
