package com.semtleWebGroup.youtubeclone.domain.video.service;

import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoLikeService {
    private final VideoLikeRepository videoLikeRepository;

    public VideoLikeResponse get(UUID videoId) {
        return VideoLikeResponse.builder()
            .likeCount(0) // TODO: video에서 like 수를 반환하는 메서드가 있어야 함.
            .isLike(false) // TODO: 토큰을 확인해서 해당 채널-비디오 id가 맞는 like가 있는지 확인해야 함. like/dislike 기능 구현되면 수정할 예정.
            .build();
    }
}
