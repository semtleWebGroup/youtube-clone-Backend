package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelProfile;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelRequest;
import com.semtleWebGroup.youtubeclone.domain.channel.exception.TitleDuplicateException;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    @Transactional
    public Channel addChannel(ChannelProfile dto, MultipartFile profileImg){
        Channel newChannel = Channel.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
        if (profileImg != null){
            saveChannelImgFromDto(profileImg, newChannel);
        }

        channelRepository.save(newChannel);

        return newChannel;
    }

    private static void saveChannelImgFromDto(MultipartFile profileImg, Channel newChannel){
        try {
            Blob blob = new SerialBlob(profileImg.getBytes());
            newChannel.setChannelImage(blob);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Channel getChannel(Long id){
        Channel channel = channelRepository.findById(id).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", id)
        ));

        return channel;
    }

    @Transactional
    public Channel updateChannel(Long id, ChannelProfile dto, MultipartFile profileImg){
        Channel oldChannel = channelRepository.findById(id).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", id)
        ));
        oldChannel.update(dto.getTitle(), dto.getDescription());

        if (profileImg != null)  saveChannelImgFromDto(profileImg, oldChannel);

        channelRepository.save(oldChannel);

        return oldChannel;
    }


    @Transactional
    public void deleteChannel(Long channelId){
        Channel oldChannel = channelRepository.findById(channelId).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", channelId)
        ));

        channelRepository.delete(oldChannel);
    }

    public List<Channel> getAllChannel(){

        return channelRepository.findAll();
    }

    public void checkTitleValid(String title){
        if (channelRepository.existsByTitle(title)){
            throw new TitleDuplicateException(title);
        }
    }
}
