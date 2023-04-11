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


    /**
     * 헤더에 맞는 ResourceRegion 을 판단하는 로직
     * 헤더를 체크하는 로직은 컨트롤러에서 처리 해줘야함
     *
     * @param ranges : Http range 헤더
     * @param videoId : video 테이블 pk
     * @return ResourceRegion
     * @throws BadRequestException : DB에 없는 UUID로 동영상을 요청할 때 보안을 위해 Bad Request
     * @throws VideoFileNotExistException : DB에는 잘 있는데, 로컬 파일이 존재하지 않는 경우
     */
    public List<ResourceRegion> createResourceRegion(List<HttpRange> ranges, UUID videoId) throws BadRequestException,VideoFileNotExistException{

        //동영상 파일 정보 불러오기
        VideoMedia videoMedia = videoMediaRepository
                .findById(videoId)
                .orElseThrow(() -> new BadRequestException(Collections.emptyList())); //보안을 위해 NotFound 대신 BadRequest 응답 - 내부 정보

        //동영상 파일 가져와서 ResourceRegion 으로
        FileSystemResource video = new FileSystemResource(videoMedia.getFilePath());
        List<ResourceRegion> resourceRegions;
        try {
            if (ranges.size() == 1) { //Range 한개
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
