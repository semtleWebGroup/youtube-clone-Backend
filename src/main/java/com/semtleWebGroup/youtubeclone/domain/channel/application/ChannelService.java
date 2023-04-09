package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelRequest;
import com.semtleWebGroup.youtubeclone.domain.channel.exception.TitleDuplicateException;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
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
        saveChannelImgFromDto(dto, newChannel);

        channelRepository.save(newChannel);

        return newChannel;
    }

    private static void saveChannelImgFromDto(ChannelRequest dto, Channel newChannel) throws IOException {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/webapp/";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + dto.getProfile_img().getOriginalFilename();
        Path savePath = Paths.get(projectPath,fileName).toAbsolutePath();
        dto.getProfile_img().transferTo(savePath);
        newChannel.setImageName(fileName);
        newChannel.setImagePath("/resources/webapp/" + fileName);
    }

    public Channel getChannel(Long id){
        Channel channel = channelRepository.findById(id).orElseThrow(()->new NoSuchElementException());

        return channel;
    }

    @Transactional
    public Channel updateChannel(Long id, ChannelRequest dto) throws IOException {
        Channel oldChannel = channelRepository.findById(id).orElseThrow(()->new NoSuchElementException("해당 채널이 없습니다."));
        oldChannel.update(dto.getChannelProfile().getTitle(), dto.getChannelProfile().getDescription());

        deleteChannelImgFromEntity(oldChannel);
        if (dto.getProfile_img() != null)  saveChannelImgFromDto(dto, oldChannel);

        channelRepository.save(oldChannel);

        return oldChannel;
    }

    private void deleteChannelImgFromEntity(Channel oldChannel) {
        String filePath = System.getProperty("user.dir") + "/src/main" + oldChannel.getImagePath();
        File file = new File(filePath);
        file.delete();
    }

    @Transactional
    public void deleteChannel(Long channelId){
        Channel oldChannel = channelRepository.findById(channelId).orElseThrow(()->new NoSuchElementException("해당 채널이 없습니다."));
        deleteChannelImgFromEntity(oldChannel);
        channelRepository.deleteById(channelId);
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
