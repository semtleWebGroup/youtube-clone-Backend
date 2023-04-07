package com.semtleWebGroup.youtubeclone.domain.video_media.api;

import com.semtleWebGroup.youtubeclone.domain.video_media.application.VideoEncodingCallBack;
import com.semtleWebGroup.youtubeclone.domain.video_media.application.VideoMediaService;
import com.semtleWebGroup.youtubeclone.domain.video_media.application.VideoStreamingService;
import com.semtleWebGroup.youtubeclone.domain.video_media.dto.SseVideoEncodingDto;
import com.semtleWebGroup.youtubeclone.domain.video_media.exception.VideoFileNotExistException;
import com.semtleWebGroup.youtubeclone.domain.video_media.util.ResourceRegionFactory;
import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.FieldError;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import com.semtleWebGroup.youtubeclone.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VideoMediaController {

    private final VideoMediaService videoMediaService;
    private final VideoStreamingService videoStreamingService;

    @Value("${streaming.max-chunk-size}")
    public long MAX_CHUNK_SIZE ; //한번에 최대 보낼 길이

    /**
     * 비디오 스트리밍 API
     * @param httpHeaders : Http request 헤더
     * @param mediaId : mediaId
     * @return HttpResponse
     */
    @GetMapping("/medias/{mediaId}")
    public ResponseEntity<List<ResourceRegion>> videoStreaming(@RequestHeader HttpHeaders httpHeaders,
                                                               @PathVariable("mediaId") UUID mediaId){

        //Range 추출 및 검증 로직
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

        List<ResourceRegion> resourceRegions = videoStreamingService.createResourceRegion(ranges, mediaId);
        MediaType mediaType = MediaTypeFactory.getMediaType(resourceRegions.get(0).getResource()).orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(mediaType)
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


