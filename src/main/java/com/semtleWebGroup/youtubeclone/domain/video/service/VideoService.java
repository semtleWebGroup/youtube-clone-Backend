package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoEditDto;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoResponse;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoUploadDto;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoViewResponse;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import com.semtleWebGroup.youtubeclone.global.error.exception.MediaServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoLikeService videoLikeService;
    private final MediaServerSpokesman mediaServerSpokesman;
    private final ChannelRepository channelRepository; // Upload를 위해 임시 사용

    private Video getVideo(UUID videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(()-> new EntityNotFoundException("Video is not found."));
        return video;
    }

    public VideoResponse upload(VideoUploadDto dto) throws MediaServerException {
        // TODO: 채널 받게끔 수정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        Channel channel = new Channel("title", "description",member);
        channelRepository.save(channel);

        Video video = Video.builder()
                .channel(channel)
                .build();
        videoRepository.save(video);

        mediaServerSpokesman.sendEncodingRequest(dto.getVideoFile(), video.getVideoId(), dto.getThumbImg());
        return new VideoResponse(video);
    }

    @Transactional
    public VideoViewResponse view(UUID videoId) {
        Video video = this.getVideo(videoId);
        video.incrementViewCount();
        videoRepository.save(video);

        VideoViewResponse videoViewResponse = VideoViewResponse.builder()
            .video(video)
            .videoLike(videoLikeService.get(video.getVideoId()))
//                .qualityList(mediaServerSpokesman.getQualityList(video.getVideoId())) // TODO
            .build();
        return videoViewResponse;
    }

    @Transactional
    public VideoResponse edit(VideoEditDto dto) {
        Video video = this.getVideo(dto.getVideoId());
        if (dto.getThumbImg() == null)
            video.update(dto.getTitle(), dto.getDescription());
        else
            video.update(dto.getTitle(), dto.getDescription(), dto.getThumbImg());
        videoRepository.save(video);
        return new VideoResponse(video);
    }

    @Transactional
    public VideoResponse delete(UUID videoId) throws MediaServerException {
        mediaServerSpokesman.deleteVideo(videoId);
        videoLikeService.delete(videoId);
        Video video = this.getVideo(videoId);
        videoRepository.delete(video);
        return new VideoResponse(video);
    }
}
