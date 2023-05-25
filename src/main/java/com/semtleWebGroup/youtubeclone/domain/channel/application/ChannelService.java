package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.auth.dao.MemberRepository;
import com.semtleWebGroup.youtubeclone.domain.auth.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelDto;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelProfile;
import com.semtleWebGroup.youtubeclone.domain.channel.exception.TitleDuplicateException;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import com.semtleWebGroup.youtubeclone.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final MemberRepository memberRepository;
    private final ChannelOwnerCheckService channelOwnerCheckService; // 테스트 코드 작성을 위해 분리

    @Transactional
    public ChannelDto addChannel(ChannelProfile dto, MultipartFile profileImg, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BadCredentialsException("token claim - memberId is not valid"));
        Channel newChannel = Channel.builder()
                .member(member)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
        if (profileImg != null){
            saveChannelImgFromDto(profileImg, newChannel);
        }

        member.getChannels().add(newChannel);
        Channel createdChannel = channelRepository.save(newChannel);
        ChannelDto response = Optional.of(createdChannel).map(ChannelDto::new)
                .orElseThrow(()->new InvalidValueException("dto 매핑 변수가 맞지않음"));

        return response;
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

    public ChannelDto getChannel(Long channelId){
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(()->new EntityNotFoundException(String.format("%d is not found.", channelId)));
        ChannelDto response = Optional.of(channel).map(ChannelDto::new)
                .orElseThrow(()->new InvalidValueException("dto 매핑 변수가 맞지않음"));

        return response;
    }

    @Transactional
    public ChannelDto updateChannel(Long id, ChannelProfile dto, MultipartFile profileImg, Long memberId){
        Channel oldChannel = channelRepository.findById(id).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", id)
        ));
        // 채널이 멤버의 소유인지 확인
        channelOwnerCheckService.checkChannelOwner(oldChannel,memberId);

        oldChannel.update(dto.getTitle(), dto.getDescription());

        if (profileImg != null)  saveChannelImgFromDto(profileImg, oldChannel);

        Channel updatedChannel = channelRepository.save(oldChannel);
        ChannelDto response = Optional.of(updatedChannel).map(ChannelDto::new)
                .orElseThrow(()->new InvalidValueException("dto 매핑 변수가 맞지않음"));

        return response;
    }


    @Transactional
    public void deleteChannel(Long channelId, Long memberId){
        Channel oldChannel = channelRepository.findById(channelId).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", channelId)
        ));
        // 채널이 멤버의 소유인지 확인
        channelOwnerCheckService.checkChannelOwner(oldChannel,memberId);

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
