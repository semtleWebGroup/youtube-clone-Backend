package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelRequest;
import com.semtleWebGroup.youtubeclone.domain.channel.exception.TitleDuplicateException;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    @Transactional
    public Channel addChannel(ChannelRequest dto) throws Exception{
        Channel newChannel = Channel.builder()
                .title(dto.getChannelProfile().getTitle())
                .description(dto.getChannelProfile().getDescription())
                .build();
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/webapp/";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + dto.getProfile_img().getOriginalFilename();
        Path savePath = Paths.get(projectPath,fileName).toAbsolutePath();
        dto.getProfile_img().transferTo(savePath);
        newChannel.setImageName(fileName);
        newChannel.setImagePath("/resources/webapp/" + fileName);

        channelRepository.save(newChannel);

        return newChannel;
    }

    public Channel getChannel(Integer id){
        Channel channel = channelRepository.findById(id).orElseThrow(()->new NoSuchElementException());

        return channel;
    }

    @Transactional
    public Channel updateChannel(Integer id, ChannelRequest dto) throws IOException {
        Channel oldChannel = channelRepository.findById(id).orElseThrow(()->new NoSuchElementException("해당 채널이 없습니다."));
        oldChannel.update(dto.getChannelProfile().getTitle(), dto.getChannelProfile().getDescription());

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/webapp/";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + dto.getProfile_img().getOriginalFilename();
        Path savePath = Paths.get(projectPath,fileName).toAbsolutePath();
        dto.getProfile_img().transferTo(savePath);
        oldChannel.setImageName(fileName);
        oldChannel.setImagePath("/resources/webapp/" + fileName);

        channelRepository.save(oldChannel);

        return oldChannel;
    }

    public List<Channel> getAllChannel(){

        return channelRepository.findAll();
    }

    public void checkTitleValid(String title){
        if (channelRepository.existsByTitle(title)){
            throw new TitleDuplicateException(title);
        }
        return;
    }
}
