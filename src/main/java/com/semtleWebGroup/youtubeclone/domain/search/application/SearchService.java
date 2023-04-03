package com.semtleWebGroup.youtubeclone.domain.search.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelDto;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoDto;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ChannelRepository channelRepository;
    public List<VideoDto> getContents(String key) {
        List<Video> videos = videoRepository.findByTitleContaining("%"+key+"%");
        return null;
    }

    public List<ChannelDto> getChannels(String key) {
        List<Channel> channels = channelRepository.findByTitleContaining("%"+key+"%");
        return null;
    }
}
