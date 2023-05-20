package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.*;
import com.semtleWebGroup.youtubeclone.domain.video.exception.ForbiddenException;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final MediaServerSpokesman mediaServerSpokesman;
    
    private void checkAuthority(Video video, Channel channel) {
        if (channel == null || video.getChannel().getId() != channel.getId())
            throw new ForbiddenException(ErrorCode.ACCESS_DENIED);
    }
    
    public Video getVideo(UUID videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(()-> new EntityNotFoundException("Video is not found."));
        return video;
    }
    
    public void save(Video video) {
        videoRepository.save(video);
    }
    
    @Transactional
    public VideoResponse upload(VideoUploadDto dto) {
        Video video = Video.builder()
                .channel(dto.getChannel())
                .build();
        videoRepository.save(video);
        mediaServerSpokesman.sendEncodingRequest(dto.getVideoFile(), video.getVideoId(), dto.getThumbImg());
        return new VideoResponse(video);
    }
    
    @Transactional
    public VideoViewResponse view(UUID videoId, Channel channel) {
        Video video = this.getVideo(videoId);
        
        // 비디오가 PUBLIC이 아니면서 권한도 없으면 에러
        if (video.getStatus() != Video.VideoStatus.PUBLIC) {
            checkAuthority(video, channel);
        }
        
        video.incrementViewCount();
        videoRepository.save(video);
        
        VideoViewResponse videoViewResponse = VideoViewResponse.builder()
                .video(video)
                .isLike(video.isLike(channel))
//                .qualityList(mediaServerSpokesman.getQualityList(video.getVideoId())) // TODO
                .build();
        return videoViewResponse;
    }
    
    @Transactional
    public VideoResponse edit(VideoEditDto dto) {
        Video video = this.getVideo(dto.getVideoId());
        checkAuthority(video, dto.getChannel());
        video.update(dto.getTitle(), dto.getDescription());
        videoRepository.save(video);
        return new VideoResponse(video);
    }
    
    @Transactional
    public VideoResponse delete(UUID videoId, Channel channel) {
        Video video = this.getVideo(videoId);
        checkAuthority(video, channel);
        
        mediaServerSpokesman.deleteVideo(videoId);
        
        videoRepository.delete(video);
        return new VideoResponse(video);
    }
    
    public VideoPageResponse findAll(Pageable pageable) {
        Page<Video> videos = videoRepository.findAllByOrderByCreatedTimeDesc(pageable);
//        Page<Video> videos = videoRepository.findByStatusOrderByCreatedTimeDesc(pageable, Video.VideoStatus.PUBLIC); // TODO: Enum으로 찾는 방법 ..
        return new VideoPageResponse(videos);
    }
}