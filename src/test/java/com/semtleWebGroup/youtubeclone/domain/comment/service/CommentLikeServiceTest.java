package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.test_super.MockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CommentLikeServiceTest extends MockTest {
    private static CommentLikeService commentLikeService;
    private static CommentRepository commentRepository;
    private static ChannelRepository channelRepository;
    @BeforeAll
    public static void set() {
        commentRepository = Mockito.mock(CommentRepository.class);
        channelRepository = Mockito.mock(ChannelRepository.class);
        commentLikeService = new CommentLikeService(commentRepository);
    }

    @Nested
    @DisplayName("좋아요 메서드")
    class 좋아요 {
        @Test
        @DisplayName("좋아요 테스트 - 성공")
        void testAdd() {
            // given
            Channel channel1 = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            Channel channel2 = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();
            Channel channel3 = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();

            Comment comment = Comment.builder()
                    .contents("테스트")
                    .build();

            when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
            when(commentRepository.save(comment)).thenReturn(comment);
            when(channelRepository.findById(channel1.getId())).thenReturn(Optional.of(channel1));
            when(channelRepository.findById(channel2.getId())).thenReturn(Optional.of(channel2));
            when(channelRepository.findById(channel3.getId())).thenReturn(Optional.of(channel3));
            // when
            commentLikeService.like(comment.getId(), channel1);
            CommentLikeResponse commentLikeResponse = commentLikeService.like(comment.getId(), channel2);
            CommentLikeResponse commentLikeResponse2 = commentLikeService.get(comment, channel3);
            // then
            assertEquals(2, commentLikeResponse.getLikeCount());
            assertTrue(commentLikeResponse.isLike());
            assertEquals(2, commentLikeResponse2.getLikeCount());
            assertFalse(commentLikeResponse2.isLike());
        }
    }

    @Nested
    @DisplayName("좋아요삭제 메서드")
    class 좋아요삭제 {
        @Test
        @DisplayName("좋아요삭제 테스트 - 성공")
        void testAdd() {
            // given
            Channel channel = Channel.builder()
                    .title("Title")
                    .description("Description")
                    .build();

            Comment comment = Comment.builder()
                    .contents("테스트")
                    .build();

            when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
            when(commentRepository.save(comment)).thenReturn(comment);
            when(channelRepository.findById(channel.getId())).thenReturn(Optional.of(channel));

            // when
            CommentLikeResponse commentLikeResponse = commentLikeService.unlike(comment.getId(), channel);

            // then
            assertEquals(0, commentLikeResponse.getLikeCount());
            assertFalse(commentLikeResponse.isLike());
        }
    }
}