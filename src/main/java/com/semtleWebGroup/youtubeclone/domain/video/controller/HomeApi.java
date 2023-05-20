package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoPageResponse;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeApi {
    private final VideoService videoService;
    
    @GetMapping("")
    public ResponseEntity getList(Pageable pageable) {
        VideoPageResponse videos = videoService.findAll(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videos);
    }
}