package com.semtleWebGroup.youtubeclone.domain.video.controller;

import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoPageResponse;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("video/search")
public class SearchApi {
    private final VideoSearchService searchService;

    /**
     * @param keyword
     * @param pageable
     * @return
     * @description 단어가 title에 포함되어 있는 video 감색, 최신순으로
     */
    @GetMapping("")
    public ResponseEntity<VideoPageResponse> searchVideos(@RequestParam("keyword") String keyword, Pageable pageable) {
        VideoPageResponse videos = searchService.searchByTitleContaining(keyword, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videos);
    }

    /**
     * @return
     * @description 랜덤으로 video를 가져오는 기능
     */
    @GetMapping("/recommend")
    public ResponseEntity<VideoPageResponse> recommendVideos(Pageable pageable) {
        VideoPageResponse videos = searchService.getAllVideosRandomOrder(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videos);
    }
}
