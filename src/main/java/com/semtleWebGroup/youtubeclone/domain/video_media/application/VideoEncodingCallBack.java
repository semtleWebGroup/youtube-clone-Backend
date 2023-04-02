package com.semtleWebGroup.youtubeclone.domain.video_media.application;

import java.io.IOException;

public interface VideoEncodingCallBack {
    public void onStart() throws IOException;
    public void onProgress(int percent) throws IOException;
    public void onComplete();
    public void onError(Throwable e);
}
