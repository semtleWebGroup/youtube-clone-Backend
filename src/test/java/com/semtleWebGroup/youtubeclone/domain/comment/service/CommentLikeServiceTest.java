package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.CommentLike;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentLikeRepository;
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
    private static CommentLikeRepository commentLikeRepository;
    private static CommentLikeService commentLikeService;
    private static CommentRepository commentRepository;
    private static CommentService commentService;
    private static ChannelRepository channelRepository;

    @BeforeAll
    public static void set() {
        channelRepository = Mockito.mock(ChannelRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        commentLikeRepository = Mockito.mock(CommentLikeRepository.class);
        commentService = new CommentService(commentRepository);
        commentLikeService = new CommentLikeService(commentRepository, commentLikeRepository);
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

            Long commentId = 1L;
            Comment comment = Comment.builder()
                    .contents("테스트")
                    .build();
            //comment.setId(commentId);

            CommentLike commentLike1 = CommentLike.builder()
                    .channel(channel1)
                    .comment(comment)
                    .build();
            CommentLike commentLike2 = CommentLike.builder()
                    .channel(channel2)
                    .comment(comment)
                    .build();


            when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
            when(commentRepository.save(comment)).thenReturn(comment);
            when(commentLikeRepository.save(commentLike1)).thenReturn(commentLike1);
            when(commentLikeRepository.save(commentLike2)).thenReturn(commentLike2);
            // when
            commentLikeService.LikeAdd(commentId, channel1);
            CommentLikeResponse commentLikeResponse = commentLikeService.LikeAdd(commentId, channel2);
            CommentLikeResponse commentLikeResponse2 = commentLikeService.get(comment, channel3);
            // then
            assertEquals(2, commentLikeResponse.getLikeCount());
            assertTrue(commentLikeResponse.isLike());
            assertEquals(2, commentLikeResponse2.getLikeCount());
            assertTrue(!commentLikeResponse2.isLike());
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

            Long commentId = 1L;
            Comment comment = Comment.builder()
                    .contents("테스트")
                    .build();
            //comment.setId(commentId);

            CommentLike commentLike1 = CommentLike.builder()
                    .channel(channel)
                    .comment(comment)
                    .build();
            comment.getLikes().add(commentLike1);

            when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
            when(commentRepository.save(comment)).thenReturn(comment);
            when(commentLikeRepository.findByCommentAndChannel(comment, channel)).thenReturn(commentLike1);

            // when
            CommentLikeResponse commentLikeResponse = commentLikeService.LikeDelete(commentId, channel);

            // then
            assertEquals(0, commentLikeResponse.getLikeCount());
            assertTrue(!commentLikeResponse.isLike());
        }
    }
}