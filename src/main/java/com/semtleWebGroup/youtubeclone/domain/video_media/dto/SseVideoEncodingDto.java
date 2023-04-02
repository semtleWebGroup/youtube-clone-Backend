package com.semtleWebGroup.youtubeclone.domain.video_media.dto;

import lombok.Getter;

@Getter
public class SseVideoEncodingDto {

    public enum Status{START,PROGRESS,COMPLETE};



    private final String event = "encodingVideo";
    private final int videoId;
    private Status status;
    private int progress;

    public SseVideoEncodingDto(int videoId, Status status, int progress) {
        this.videoId = videoId;
        this.status = status;
        this.progress = progress;
    }

    public static SseVideoEncodingDto encodingStartDto(int videoId){
        return new SseVideoEncodingDto(videoId,Status.START,0);
    }
    public static SseVideoEncodingDto encodingProgressDto(int videoId,int progress){
        return new SseVideoEncodingDto(videoId,Status.PROGRESS,progress);
    }
    public static SseVideoEncodingDto encodingCompleteDto(int videoId){
        return new SseVideoEncodingDto(videoId,Status.COMPLETE,100);
    }



}
