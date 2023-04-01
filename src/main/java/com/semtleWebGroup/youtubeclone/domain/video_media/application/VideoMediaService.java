package com.semtleWebGroup.youtubeclone.domain.video_media.application;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VideoMediaService {


    /**
     * 인코딩을 위한 메서드
     * Server-sent-event 를 통해 몇프로 진행이 되었는지 알려준다.
     * 비동기 처리 예정 @Async
     *
     * @param videoId : videoId
     * @param callBack : Server-sent-event 를 위한 인터페이스
     */
    public void encodeVideo(int videoId, VideoEncodingCallBack callBack) throws IOException {
        //일단은 목업이니까 바로 종료
        callBack.onStart();
        callBack.onProgress(10);
        callBack.onProgress(30);
        callBack.onProgress(40);
        callBack.onComplete();

    }

}
