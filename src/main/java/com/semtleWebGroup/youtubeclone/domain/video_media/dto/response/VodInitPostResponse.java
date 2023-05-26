package com.semtleWebGroup.youtubeclone.domain.video_media.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VodInitPostResponse {
    private String videoId;
    private String message;

    //비디오 길이 (초 단위)
    private double videoLength;
}