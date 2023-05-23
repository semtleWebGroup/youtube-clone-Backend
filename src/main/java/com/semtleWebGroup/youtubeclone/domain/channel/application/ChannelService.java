package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelRequest;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelResponse;
import com.semtleWebGroup.youtubeclone.domain.channel.exception.TitleDuplicateException;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.repository.MemberRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import com.semtleWebGroup.youtubeclone.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityListeners;
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChannelService {
    private final MemberRepository memberRepository;
    private final ChannelRepository channelRepository;
    private final JwtTokenProvider jwtTokenProvider;

//    private final JwtTokenProvider jwtTokenProvider;
    @Transactional
    public ChannelResponse addChannel(ChannelRequest dto){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Member member = (Member) authentication.getPrincipal();
            log.info("authentication 내의 memberId: {}", member.getId());
        try{
            Channel newChannel = Channel.builder()
                    .member(member)
                    .title(dto.getChannelProfile().getTitle())
                    .description(dto.getChannelProfile().getDescription())
                    .build();
            if (dto.getProfile_img() != null) {
                saveChannelImgFromDto(dto, newChannel);
            }
        
            Channel channel = channelRepository.save(newChannel);
            Member savedMember = memberRepository.findByChannels_Id(channel.getId());
            String token = jwtTokenProvider.generateMemberToken(savedMember);
            log.debug("SecurityContextHolder: {}", SecurityContextHolder.getContext().getAuthentication().toString());
            return ChannelResponse.of(channel, token);
        }
        catch(DataIntegrityViolationException e){
            //TODO: 예외 변경해줘야함
            throw new IllegalArgumentException("title already in use");
        }
    }

    private static void saveChannelImgFromDto(ChannelRequest dto, Channel newChannel){
        try {
            Blob blob = new SerialBlob(dto.getProfile_img().getBytes());
            newChannel.setChannelImage(blob);
        } catch (SQLException | IOException e) {
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
    public Channel updateChannel(Long id, ChannelRequest dto){
        Channel oldChannel = channelRepository.findById(id).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", id)
        ));
        oldChannel.update(dto.getChannelProfile().getTitle(), dto.getChannelProfile().getDescription());

        if (dto.getProfile_img() != null)  saveChannelImgFromDto(dto, oldChannel);

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
