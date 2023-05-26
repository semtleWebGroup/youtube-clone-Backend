package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.auth.dao.MemberRepository;
import com.semtleWebGroup.youtubeclone.domain.auth.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.auth.domain.Role;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelDto;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelProfile;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import com.semtleWebGroup.youtubeclone.test_super.MockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChannelServiceTest extends MockTest {
    // mock up
    private static ChannelService channelService;
    private static ChannelRepository channelRepository;
    private static MemberRepository memberRepository;
    private static ChannelOwnerCheckService channelOwnerCheckService;

    @BeforeAll
    public static void setMockChannelRepository() {
        channelRepository = Mockito.mock(ChannelRepository.class);
        memberRepository = Mockito.mock(MemberRepository.class);
        channelOwnerCheckService = Mockito.mock(ChannelOwnerCheckService.class);
        channelService = new ChannelService(channelRepository, memberRepository, channelOwnerCheckService);
    }

    @Nested
    @DisplayName("createChannel 메서드")
    class createChannel{
        @Test
        @DisplayName("addChannel 테스트 - 이미지 파일이 있을 경우")
        void testAddChannelWithImage() throws IOException, SQLException {
            // given
            MultipartFile image = new MockMultipartFile("test.jpg", "test".getBytes());
            ChannelProfile request = new ChannelProfile();
            request.setTitle("Test Channel");
            request.setDescription("Test Channel Description");

            Channel createdChannel = Channel.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .build();
            createdChannel.setChannelImage(new SerialBlob(image.getBytes()));
            Member createdMember = new Member("2222@kumoh.ac.kr","1234", Role.ROLE_USER,new ArrayList<>());

            when(channelRepository.save(any(Channel.class))).thenReturn(createdChannel);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(createdMember));

            // when
            ChannelDto channel = channelService.addChannel(request, image,1L);

            // then
            assertEquals(createdChannel.getTitle(), channel.getTitle());
            assertEquals(createdChannel.getDescription(), channel.getDescription());
            assertTrue(Arrays.equals(new SerialBlob(image.getBytes()).getBytes(1, (int) image.getSize()), channel.getChannelImage()));
        }

        @Test
        @DisplayName("addChannel 테스트 - 이미지 파일이 없을 경우")
        void testAddChannelWithoutImage(){
            // given
            ChannelProfile request = new ChannelProfile();
            MultipartFile image = null;
            request.setTitle("Test Channel");
            request.setDescription("Test Channel Description");

            Member createdMember = new Member("2222@kumoh.ac.kr","1234", Role.ROLE_USER,new ArrayList<>());
            Channel createdChannel = Channel.builder()
                    .member(createdMember)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .build();

            when(channelRepository.save(any(Channel.class))).thenReturn(createdChannel);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(createdMember));

            // when
            ChannelDto channel = channelService.addChannel(request, image, 1L);

            // then
            assertEquals(createdChannel.getTitle(), channel.getTitle());
            assertEquals(createdChannel.getDescription(), channel.getDescription());
            assertNull(channel.getChannelImage());
        }
    }

    @Nested
    @DisplayName("getChannel 메서드")
    class getChannel{
        @Test
        @DisplayName("getChannel 테스트 - 채널이 존재하는 경우")
        void testGetChannel() {
            // given
            Channel channel = Channel.builder().title("Test Channel")
                    .description("Test Channel Description").build();

            when(channelRepository.findById(1L)).thenReturn(java.util.Optional.of(channel));

            // when
            ChannelDto resultChannel = channelService.getChannel(1L);

            // then
            assertEquals(channel.getTitle(), resultChannel.getTitle());
            assertEquals(channel.getDescription(), resultChannel.getDescription());
            assertEquals(channel.getChannelImage(), resultChannel.getChannelImage());
        }

        @Test
        @DisplayName("getChannel 테스트 - 채널이 존재하지 않는 경우")
        void testGetChannelInvalidId() {
            // given
            Channel channel = Channel.builder().title("Test Channel")
                    .description("Test Channel Description").build();

            when(channelRepository.findById(1L)).thenReturn(Optional.empty());

            // then
            assertThrows(EntityNotFoundException.class, () -> {
                // when
                channelService.getChannel(1L);
            });
        }
    }

    @Nested
    @DisplayName("updateChannel 메서드")
    class updateChannel{
        @Test
        @DisplayName("채널 정보 수정 - 성공")
        void updateChannel_Success() throws IOException, SQLException {
            // given
            Long id = 1L;
            Member createdMember = new Member("2222@kumoh.ac.kr","1234", Role.ROLE_USER,new ArrayList<>());
            Channel oldChannel = Channel.builder()
                    .member(createdMember)
                    .title("Old Title")
                    .description("Old Description")
                    .build();
            MultipartFile image = new MockMultipartFile("test.jpg", "test".getBytes());
            ChannelProfile dto = new ChannelProfile();
            dto.setTitle("New Channel");
            dto.setDescription("New Description");


            when(channelRepository.findById(id)).thenReturn(Optional.of(oldChannel));
            when(channelRepository.save(any(Channel.class))).thenReturn(oldChannel);
            when(channelOwnerCheckService.checkChannelOwner(oldChannel,1L)).thenReturn(true);

            // when
            ChannelDto updatedChannel = channelService.updateChannel(id, dto, image, 1L);

            // then
            assertEquals(dto.getTitle(), updatedChannel.getTitle());
            assertEquals(dto.getDescription(), updatedChannel.getDescription());
            assertTrue(Arrays.equals(new SerialBlob(image.getBytes()).getBytes(1, (int) image.getSize()), updatedChannel.getChannelImage()));
        }

        @Test
        @DisplayName("채널 정보 수정 - 실패 : 해당 id의 채널이 존재하지 않음")
        void updateChannel_ChannelNotFound() {
            // given
            Long id = 1L;
            MultipartFile image = new MockMultipartFile("test.jpg", "test".getBytes());
            ChannelProfile dto = new ChannelProfile();
            dto.setTitle("Test Channel");
            dto.setDescription("Test Channel Description");
            when(channelRepository.findById(id)).thenReturn(Optional.empty());

            // when, then
            assertThrows(EntityNotFoundException.class, () -> channelService.updateChannel(id, dto,image,1L));
        }
    }

    @Nested
    @DisplayName("deleteChannel 메서드")
    class deleteChannel {
        @Test
        @DisplayName("채널 삭제 - 성공")
        void deleteChannel_Success() {
            // given
            Long channelId = 1L;
            Channel channel = Channel.builder()
                    .title("Old Title")
                    .description("Old Description")
                    .build();
            when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
            when(channelOwnerCheckService.checkChannelOwner(channel,1L)).thenReturn(true);

            // when
            channelService.deleteChannel(channelId,1L);

            // then
            verify(channelRepository, times(1)).delete(channel);
//            verify는 메소드가 몇번 실행되었는지 확인
        }

        @Test
        @DisplayName("채널 삭제 - 실패 : 해당 id의 채널이 존재하지 않음")
        void deleteChannel_ChannelNotFound() {
            // given
            Long channelId = 1L;
            when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

            // when & then
            assertThrows(EntityNotFoundException.class, () -> channelService.deleteChannel(channelId, 1L));
        }
    }
}