package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentViewResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import com.semtleWebGroup.youtubeclone.test_super.MockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CommentServiceTest extends MockTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired //이거 안쓰고 위에서 뭐 써주면 됨
    ChannelRepository channelRepository;

    @Test
    void 등록() {
        CommentRequest entity = new CommentRequest();
        entity.setContent("테스트");

        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        Video video = Video.builder()
                .channel(channel)
                .build();
        //When
        Comment comment = commentService.write(entity, channel, video);
        //Then
        Comment findComment = commentRepository.findById(comment.getId()).get();
        assertEquals(entity.getContent(), findComment.getContents());
    }
    @Test
    void 답글등록() {
        CommentRequest entity1 = new CommentRequest();
        entity1.setContent("댓글");
        CommentRequest entity2 = new CommentRequest();
        entity2.setContent("답글");

        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        Video video = Video.builder()
                .channel(channel)
                .build();
        Comment comment = commentService.write(entity1, channel, video);

        //When
        Comment replyComment = commentService.replyWrite(entity2, channel, video, comment.getId());
        //Then
        Comment findComment = commentRepository.findById(replyComment.getId()).get();
        assertEquals(entity2.getContent(), findComment.getContents());
    }

    @Test
    void 업데이트() {
        CommentRequest entity = new CommentRequest();
        entity.setContent("테스트");
        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        Video video = Video.builder()
                .channel(channel)
                .build();

        Comment comment = commentService.write(entity, channel, video);

        CommentRequest entity2 = new CommentRequest();
        entity2.setContent("업데이트");
        //When
        Comment comment2 = commentService.updateComment(comment.getId(), entity2);
        //Then
        Comment findComment = commentRepository.findById(comment2.getId()).get();
        assertEquals(entity2.getContent(), findComment.getContents());
    }

    @Test
    void 목록() {
        CommentRequest entity1 = new CommentRequest();
        entity1.setContent("테스트1");
        CommentRequest entity2 = new CommentRequest();
        entity2.setContent("테스트2");

        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        channelRepository.save(channel);
        Video video1 = Video.builder()
                .channel(channel)
                .build();

        Video video2 = Video.builder()
                .channel(channel)
                .build();
        videoRepository.save(video1);
        videoRepository.save(video2);

        //하나의 비디오에 한명이 다른 댓글 달기
        Comment comment1 = commentService.write(entity1, channel, video1);
        Comment comment2 = commentService.write(entity2, channel, video1);
        //다른 비디오에 댓글 달기
        Comment comment3 = commentService.write(entity1, channel, video2);
        // -> 댓글을 3개를 남겼으나 첫번째 비디오에 2개를 남기고 두번째 비디오에 1개를 남김
        
        //when
        List<CommentViewResponse> CommentList = commentService.getCommentList(video1.getId(), channel);
        //then
        assertThat(CommentList.size()).isEqualTo(2);

    }

    @Test
    void 답글목록() {
        CommentRequest entity1 = new CommentRequest();
        entity1.setContent("댓글1");
        CommentRequest entity2 = new CommentRequest();
        entity2.setContent("댓글2");
        CommentRequest entity3 = new CommentRequest();
        entity3.setContent("답글");

        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        channelRepository.save(channel);
        Video video1 = Video.builder()
                .channel(channel)
                .build();

        Video video2 = Video.builder()
                .channel(channel)
                .build();
        videoRepository.save(video1);
        videoRepository.save(video2);

        //하나의 비디오에 한명이 다른 댓글 달기
        Comment comment1 = commentService.write(entity1, channel, video1);
        Comment comment2 = commentService.write(entity2, channel, video1);
        //다른 댓글에 답글 달기
        Comment replyComment1 = commentService.replyWrite(entity3, channel, video1, comment1.getId());
        Comment replyComment2 = commentService.replyWrite(entity3, channel, video1, comment2.getId());

        //when
        List<CommentViewResponse> CommentList = commentService.getReplyList(comment1.getId(), channel);
        //then
        assertThat(CommentList.size()).isEqualTo(1);

    }

    @Test
    void 삭제() {
        CommentRequest entity = new CommentRequest();
        entity.setContent("테스트");
        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        Video video = Video.builder()
                .channel(channel)
                .build();
        Comment comment = commentService.write(entity, channel, video);

        //when
        commentService.commentDelete(comment.getId());
        //then
        List<Comment> result = commentRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }
}
