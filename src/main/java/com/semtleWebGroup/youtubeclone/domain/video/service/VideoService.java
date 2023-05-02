package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoRequest;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoViewResponse;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import com.semtleWebGroup.youtubeclone.global.error.exception.MediaServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

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

    public Video upload(MultipartFile videoFile, MultipartFile thumbImg) throws MediaServerException {
        // TODO: 채널 받게끔 수정
        Channel channel = new Channel("title", "description");
        channelRepository.save(channel);

        Video video = Video.builder()
                .channel(channel)
                .build();
        videoRepository.save(video);
        mediaServerSpokesman.sendEncodingRequest(videoFile, video.getVideoId(), thumbImg);
        return video;
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
    public Video edit(UUID videoId, VideoRequest dto, Blob thumbImg) {
        Video video = this.getVideo(videoId);
        if (thumbImg == null)
            video.update(dto.getTitle(), dto.getDescription());
        else
            video.update(dto.getTitle(), dto.getDescription(), thumbImg);
        videoRepository.save(video);
        return video;
    }

    @Transactional
    public Video delete(UUID videoId) throws MediaServerException {
        mediaServerSpokesman.deleteVideo(videoId);
        videoLikeService.delete(videoId);
        Video video = this.getVideo(videoId);
        videoRepository.delete(video);
        return video;
    }
}
