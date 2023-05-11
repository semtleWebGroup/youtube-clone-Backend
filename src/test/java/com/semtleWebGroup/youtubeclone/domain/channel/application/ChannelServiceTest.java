package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelProfile;
import com.semtleWebGroup.youtubeclone.domain.channel.dto.ChannelRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChannelServiceTest extends MockTest {
    // mock up
    private static ChannelService channelService;
    private static ChannelRepository channelRepository;

    @BeforeAll
    public static void setMockChannelRepository() {
        channelRepository = Mockito.mock(ChannelRepository.class);
        channelService = new ChannelService(channelRepository);
    }

    @Nested
    @DisplayName("createChannel 메서드")
    class createChannel{
        @Test
        @DisplayName("addChannel 테스트 - 이미지 파일이 있을 경우")
        void testAddChannelWithImage() throws IOException, SQLException {
            // given
            MultipartFile image = new MockMultipartFile("test.jpg", "test".getBytes());
            ChannelRequest request = new ChannelRequest();
            request.setChannelProfile(new ChannelProfile());
            request.getChannelProfile().setTitle("Test Channel");
            request.getChannelProfile().setDescription("Test Channel Description");
            request.setProfile_img(image);

            Channel createdChannel = Channel.builder()
                    .title(request.getChannelProfile().getTitle())
                    .description(request.getChannelProfile().getDescription())
                    .build();
            createdChannel.setProfileImg(new SerialBlob(image.getBytes()));

            when(channelRepository.save(any(Channel.class))).thenReturn(createdChannel);

            // when
            Channel channel = channelService.addChannel(request);

            // then
            assertEquals(createdChannel.getTitle(), channel.getTitle());
            assertEquals(createdChannel.getDescription(), channel.getDescription());
            assertEquals(new SerialBlob(image.getBytes()), channel.getProfileImg());
        }

        @Test
        @DisplayName("addChannel 테스트 - 이미지 파일이 없을 경우")
        void testAddChannelWithoutImage(){
            // given
            ChannelRequest request = new ChannelRequest();
            request.setChannelProfile(new ChannelProfile());
            request.getChannelProfile().setTitle("Test Channel");
            request.getChannelProfile().setDescription("Test Channel Description");

            Channel createdChannel = Channel.builder()
                    .title(request.getChannelProfile().getTitle())
                    .description(request.getChannelProfile().getDescription())
                    .build();

            when(channelRepository.save(any(Channel.class))).thenReturn(createdChannel);

            // when
            Channel channel = channelService.addChannel(request);

            // then
            assertEquals(createdChannel.getTitle(), channel.getTitle());
            assertEquals(createdChannel.getDescription(), channel.getDescription());
            assertNull(channel.getProfileImg());
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
            Channel resultChannel = channelService.getChannel(1L);

            // then
            assertEquals(channel, resultChannel);
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
            Channel oldChannel = Channel.builder()
                    .title("Old Title")
                    .description("Old Description")
                    .build();
            MultipartFile image = new MockMultipartFile("test.jpg", "test".getBytes());
            ChannelRequest dto = new ChannelRequest();
            dto.setChannelProfile(new ChannelProfile());
            dto.getChannelProfile().setTitle("New Channel");
            dto.getChannelProfile().setDescription("New Description");
            dto.setProfile_img(image);

            when(channelRepository.findById(id)).thenReturn(Optional.of(oldChannel));
            when(channelRepository.save(any(Channel.class))).thenReturn(oldChannel);

            // when
            Channel updatedChannel = channelService.updateChannel(id, dto);

            // then
            assertEquals(dto.getChannelProfile().getTitle(), updatedChannel.getTitle());
            assertEquals(dto.getChannelProfile().getDescription(), updatedChannel.getDescription());
            assertEquals(new SerialBlob(dto.getProfile_img().getBytes()), updatedChannel.getProfileImg());
        }

        @Test
        @DisplayName("채널 정보 수정 - 실패 : 해당 id의 채널이 존재하지 않음")
        void updateChannel_ChannelNotFound() {
            // given
            Long id = 1L;
            MultipartFile image = new MockMultipartFile("test.jpg", "test".getBytes());
            ChannelRequest dto = new ChannelRequest();
            dto.setChannelProfile(new ChannelProfile());
            dto.getChannelProfile().setTitle("Test Channel");
            dto.getChannelProfile().setDescription("Test Channel Description");
            dto.setProfile_img(image);
            when(channelRepository.findById(id)).thenReturn(Optional.empty());

            // when, then
            assertThrows(EntityNotFoundException.class, () -> channelService.updateChannel(id, dto));
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

            // when
            channelService.deleteChannel(channelId);

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
            assertThrows(EntityNotFoundException.class, () -> channelService.deleteChannel(channelId));
        }
    }
}