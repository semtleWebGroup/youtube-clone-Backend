package com.semtleWebGroup.youtubeclone.domain.search.api;

import com.semtleWebGroup.youtubeclone.domain.search.application.SearchService;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/video")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search/{key}")
    public ResponseEntity<List<?>> searchVideo(@PathVariable("key")String key){
        List<VideoDto> contents = searchService.getContents(key);
        return ResponseEntity.status(HttpStatus.OK).body(contents);
    }
}
