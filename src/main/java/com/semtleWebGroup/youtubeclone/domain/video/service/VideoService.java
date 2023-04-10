package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public Video add(Integer id, VideoRequest dto, MultipartFile file) throws IOException {
        // get video by id
        Video video = this.get(id);

        // save thumbnail image
        String dir = System.getProperty("user.dir") + "\\src\\main\\resources\\webapp\\thumbnails";
        String thumbImgName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File saveFile = new File(dir, thumbImgName);
        file.transferTo(saveFile);

        // update and save video
        video.update(dto.getTitle(), dto.getDescription(), thumbImgName);
        videoRepository.save(video);

        return video;
    }

    public Video get(Integer id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        String.format("%d is not found.", id)
                ));
        return video;
    }

    public Video edit(Integer id, VideoRequest dto) {
        // get video by id
        Video originVideo = this.get(id);
        Video newVideo = originVideo;

        // update and save video
        newVideo.update(dto.getTitle(), dto.getDescription());
        videoRepository.save(newVideo);

        // return original video
        return originVideo;
    }
}
