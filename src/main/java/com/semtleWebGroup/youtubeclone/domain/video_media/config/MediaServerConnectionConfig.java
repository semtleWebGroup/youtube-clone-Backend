package com.semtleWebGroup.youtubeclone.domain.video_media.config;

import com.semtleWebGroup.youtubeclone.domain.video_media.service.MediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.MockedMediaServerSpokesman;
import com.semtleWebGroup.youtubeclone.domain.video_media.service.ProdMediaServerSpokesman;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

@Configuration
public class MediaServerConnectionConfig {

    @Profile({"dev", "test"})
    @Bean
    public MediaServerSpokesman devMediaServerSpokesman(){
        return new MockedMediaServerSpokesman();
    }

    @Profile("prod")
    @Bean
    public MediaServerSpokesman prodMediaServerSpokesman(Environment environment){
        // application-prod.yml에 media-server.url이 설정되어 있어야 한다.
        String mediaServerUrl = environment.getProperty("media-server.url");
        Assert.notNull(mediaServerUrl, "media-server.url must not be null on prod profile");
        return new ProdMediaServerSpokesman(mediaServerUrl);
    }
}
