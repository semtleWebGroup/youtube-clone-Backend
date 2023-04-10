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
        Video video = this.get(id);

        // save thumbnail image
        String dir = System.getProperty("user.dir") + "\\src\\main\\resources\\webapp\\thumbnails";
        String thumbImgName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File saveFile = new File(dir, thumbImgName);
        file.transferTo(saveFile);

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
        Video video = this.get(id);

        video.update(dto.getTitle(), dto.getDescription());

        videoRepository.save(video);
        return video;
    }

    public Video addViewCount(Integer id) {
        Video video = this.get(id);

        video.incrementViewCount();

        videoRepository.save(video);
        return video;
    }
}
