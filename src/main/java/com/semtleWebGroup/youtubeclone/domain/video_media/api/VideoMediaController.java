package com.semtleWebGroup.youtubeclone.domain.video_media.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semtleWebGroup.youtubeclone.domain.video_media.application.VideoEncodingCallBack;
import com.semtleWebGroup.youtubeclone.domain.video_media.application.VideoMediaService;
import com.semtleWebGroup.youtubeclone.domain.video_media.dto.SseVideoEncodingDto;
import com.semtleWebGroup.youtubeclone.domain.video_media.exception.VideoFileNotExistException;
import com.semtleWebGroup.youtubeclone.domain.video_media.util.ResourceRegionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VideoMediaController {

    private final VideoMediaService videoMediaService;
    public static final long MAX_CHUNK_SIZE = 1_000_000L; //한번에 최대 보낼 길이

    /**
     * 목업용 간단 비디오 스트리밍 컨트롤러
     * @param httpHeaders : Http request 헤더
     * @param videoId : videoId
     * @param resolution : nullable, 요청하는 비디오의 해상도 "1080p", "720p" 와 같이 파라미터에 넣어주세요
     * @return HttpResponse
     */
    @GetMapping("/videos/{videoId}/media")
    public ResponseEntity<List<ResourceRegion>> videoStreaming(@RequestHeader HttpHeaders httpHeaders,
                                                               @PathVariable("videoId") int videoId,
                                                               @RequestParam(value = "resolution",required = false) String resolution){

        if (resolution == null){
            //화질이 설정되지 않았을 경우 적절한 처리
        }

        //파일 불러오기
        Resource resource = new FileSystemResource(Paths.get("src","main","java","com","semtleWebGroup","youtubeclone","domain","video_media","storage","for_mock","file_example_MP4_1920_18MG.mp4"));
        Assert.isTrue(resource.exists(), () -> "sample file not exist");

        //length 확인
        long contentLength;
        try {
            contentLength = resource.contentLength();
        } catch (IOException e) {
            //물리적 파일이 존재하지 않음
            throw new VideoFileNotExistException(resource);
        }

        //ResourceRegion 만들기
        List<ResourceRegion> resourceRegions;
        List<HttpRange> ranges = httpHeaders.getRange();
        try {
            if (CollectionUtils.isEmpty(ranges)) { //Range 안줌
                resourceRegions = Collections.emptyList();
            } else if (ranges.size() == 1) { //Range 한개
                resourceRegions = List.of(ResourceRegionFactory.fromRange(ranges.get(0), resource, MAX_CHUNK_SIZE));
            } else { // Range 여러개
                resourceRegions = ResourceRegionFactory.fromRanges(ranges, resource, MAX_CHUNK_SIZE);
            }
        } catch (IOException e) {
            throw new VideoFileNotExistException(resource);
        }

        //응답 보내기
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegions);
    }

    /**
     * 간단한 목업용 비디오 업로드 URL
     * @param file 작성된 비디오 파일
     * @return 파일이 비었을 경우 BadRequest, 파일이 있는 경우 OK
     */
    @PostMapping("/videos/media")
    public ResponseEntity<SseEmitter> videoPosting(@RequestParam(value = "video",required = true) MultipartFile file) throws IOException {
        final SseEmitter emitter = new SseEmitter(30*1000L);

        //videoId 생성로직
        int videoId = 1234;

        videoMediaService.encodeVideo(videoId, new VideoEncodingCallBack() {
            private final int id = videoId;
            @Override
            public void onStart() throws IOException {
                //send Dummy data
                emitter.send(
                        SseEmitter.event().data(
                                SseVideoEncodingDto.encodingStartDto(id)
                        )
                );

            }

            @Override
            public void onProgress(int percent) throws IOException {
                emitter.send(
                        SseEmitter.event().data(
                                SseVideoEncodingDto.encodingProgressDto(id,percent)
                        )
                );

            }

            @Override
            public void onComplete() {
                try {
                    emitter.send(
                            SseEmitter.event().data(
                                    SseVideoEncodingDto.encodingCompleteDto(id)
                            )
                    );
                } catch (IOException e){
                    emitter.complete();
                } finally {
                    emitter.complete();
                }
            }

            @Override
            public void onError(Throwable e) {
                emitter.completeWithError(e);

            }
        });

        return ResponseEntity.ok(emitter);
    }
}


