package com.semtleWebGroup.youtubeclone.domain.video_media.dto.response;

import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class GetEncodingStatusResponse {
    public enum EncodingStatus {WAIT, RUNNING, FINISHED, ERROR}
    public enum MediaType {VIDEO, THUMBNAIL};
    private EncodingStatus entireJobStatus;
    private Map<MediaType , EncodingStatus> statusMap;


}
