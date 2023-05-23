package com.semtleWebGroup.youtubeclone.domain.video.dto;

import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VideoPageResponse {
    private int totalPages;
    private int number; // page number
    private int numberOfElements;
    
    private List<VideoListResponse> videos = new ArrayList<>();
    
    public VideoPageResponse(Page<Video> videoPage) {
        this.totalPages = videoPage.getTotalPages();
        this.number = videoPage.getNumber();
        this.numberOfElements = videoPage.getNumberOfElements();
        for (Video video: videoPage.getContent()) {
            videos.add(new VideoListResponse(video));
        }
    }
}