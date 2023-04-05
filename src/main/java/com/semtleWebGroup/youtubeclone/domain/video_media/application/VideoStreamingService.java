package com.semtleWebGroup.youtubeclone.domain.video_media.application;



import com.semtleWebGroup.youtubeclone.domain.video_media.domain.VideoMedia;
import com.semtleWebGroup.youtubeclone.domain.video_media.exception.VideoFileNotExistException;
import com.semtleWebGroup.youtubeclone.domain.video_media.repository.VideoMediaRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.util.ResourceRegionFactory;
import com.semtleWebGroup.youtubeclone.global.error.FieldError;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoStreamingService {

    @Value("${streaming.max-chunk-size}")
    public long MAX_CHUNK_SIZE ; //한번에 최대 보낼 길이

    private final VideoMediaRepository videoMediaRepository;

    public List<ResourceRegion> createResourceRegion(HttpHeaders httpHeaders, UUID videoId){

        //Range 추출
        List<HttpRange> ranges;
        if (!httpHeaders.containsKey("Range")){
            throw new BadRequestException(FieldError.of("Range","null","Request With No Range"));
        }
        try {
            ranges = httpHeaders.getRange();
        } catch (IllegalArgumentException e){
            throw new BadRequestException(FieldError.of("Range","invalid",e.getMessage()));
        }
        if (CollectionUtils.isEmpty(ranges)){
            throw new BadRequestException(FieldError.of("Range","null","Request With Empty Range"));
        }

        //동영상 가져오기
        VideoMedia videoMedia = videoMediaRepository
                .findById(videoId)
                .orElseThrow(() -> new BadRequestException(Collections.emptyList())); //보안을 위해

        FileSystemResource video = new FileSystemResource(videoMedia.getFilePath());
        List<ResourceRegion> resourceRegions;
        try {
            if (CollectionUtils.isEmpty(ranges)) { //Range 안줌
                resourceRegions = Collections.emptyList();
            } else if (ranges.size() == 1) { //Range 한개
                resourceRegions = List.of(ResourceRegionFactory.fromRange(ranges.get(0), video, MAX_CHUNK_SIZE));
            } else { // Range 여러개
                resourceRegions = ResourceRegionFactory.fromRanges(ranges, video, MAX_CHUNK_SIZE);
            }
        } catch (IOException e) {
            throw new VideoFileNotExistException(video);
        }

        return resourceRegions;


    }
}
