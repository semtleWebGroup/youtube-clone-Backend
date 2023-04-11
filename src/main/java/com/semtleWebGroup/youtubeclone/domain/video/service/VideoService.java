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
import java.util.UUID;

@Service
public class VideoService {
    @Autowired
    private VideoInfoRepository videoInfoRepository;

    @Transactional
    public VideoInfo add(UUID id, VideoRequest dto, Blob thumbImg) {
        VideoInfo video = this.get(id);
        video.update(dto.getTitle(), dto.getDescription(), thumbImg);
        videoInfoRepository.save(video);
        return video;
    }

    private VideoInfo get(UUID id) {
//        VideoInfo videoInfo = videoInfoRepository.findById(id)
//                .orElseThrow(()-> new EntityNotFoundException(
//                        String.format("%d is not found.", id)
//                ));
        // TODO: videoId를 이용하여 videoInfoId를 찾아서 videoInfo를 가져와야 함.
//        return videoInfo;
        return null;
    }

    @Transactional
    public VideoInfo edit(UUID id, VideoRequest dto) {
        VideoInfo video = this.get(id);
        video.update(dto.getTitle(), dto.getDescription());
        videoInfoRepository.save(video);
        return video;
    }

    @Transactional
    public VideoInfo view(UUID id) {
        // TODO: channel 관련 정보, media type list도 join해서 보내줄 것
        VideoInfo video = this.get(id);
        video.incrementViewCount();
        videoInfoRepository.save(video);
        return video;
    }
}
