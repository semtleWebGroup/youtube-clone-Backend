package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.*;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommentServiceTest{
    @Autowired  //테스트코드에서는 @RequiredArgsConstructor 사용 불가
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
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
        channelRepository.save(channel);
        videoRepository.save(video);
        //When
        CommentResponse comment = commentService.write(entity, channel, video);
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
        channelRepository.save(channel);
        videoRepository.save(video);
        CommentResponse comment = commentService.write(entity1, channel, video);

        //When
        CommentResponse replyComment = commentService.replyWrite(entity2, channel, comment.getId());
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
        channelRepository.save(channel);
        videoRepository.save(video);
        CommentResponse comment = commentService.write(entity, channel, video);

        CommentRequest entity2 = new CommentRequest();
        entity2.setContent("업데이트");
        //When
        CommentResponse comment2 = commentService.updateComment(comment.getId(), entity2, channel);
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
        CommentResponse comment1 = commentService.write(entity1, channel, video1);
        CommentResponse comment2 = commentService.write(entity2, channel, video1);
        //다른 비디오에 댓글 달기
        CommentResponse comment3 = commentService.write(entity1, channel, video2);
        // -> 댓글을 3개를 남겼으나 첫번째 비디오에 2개를 남기고 두번째 비디오에 1개를 남김

        //when
        Pageable pageable = PageRequest.of(0, 5);
        CommentPageResponse CommentList = commentService.getCommentList(video1.getId(), channel, pageable);
        //then
        assertThat(CommentList.getComments().size()).isEqualTo(2);

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
        CommentResponse comment1 = commentService.write(entity1, channel, video1);
        CommentResponse comment2 = commentService.write(entity2, channel, video1);
        //다른 댓글에 답글 달기
        CommentResponse replyComment1 = commentService.replyWrite(entity3, channel, comment1.getId());
        CommentResponse replyComment2 = commentService.replyWrite(entity3, channel, comment2.getId());

        //when
        Pageable pageable = PageRequest.of(0, 5);
        CommentPageResponse CommentList = commentService.getReplyList(comment1.getId(), channel, pageable);
        //then
        assertThat(CommentList.getComments().size()).isEqualTo(1);

    }

    @Test
    void 삭제() {
        CommentRequest entity = new CommentRequest();
        entity.setContent("테스트");
        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        channelRepository.save(channel);
        Video video = Video.builder()
                .channel(channel)
                .build();
        videoRepository.save(video);
        CommentResponse comment = commentService.write(entity, channel, video);

        //when
        commentService.commentDelete(comment.getId(), channel);
        //then
        List<Comment> result = commentRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }
    @Test
    void 부모댓글삭제() {  //부모 댓글 삭제시 자동으로 답글 삭제 테스트
        CommentRequest entity1 = new CommentRequest();
        entity1.setContent("테스트");
        CommentRequest entity2 = new CommentRequest();
        entity2.setContent("답글");
        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        channelRepository.save(channel);
        Video video = Video.builder()
                .channel(channel)
                .build();
        videoRepository.save(video);
        CommentResponse comment = commentService.write(entity1, channel, video);
        CommentResponse replyComment = commentService.replyWrite(entity2, channel, comment.getId());
        //when
        commentService.commentDelete(comment.getId(), channel);
        //then
        assertThat(channel.getComments().size()).isEqualTo(0); //부모댓글을 삭제했으니 답글도 삭제될 것이고 개수가 0개여야함
        assertThat(video.getComments().size()).isEqualTo(0);
        List<Comment> result = commentRepository.findAll();
        assertThat(result.size()).isEqualTo(0);  //부모댓글을 삭제했으니 답글도 삭제될 것이고 개수가 0개여야함
    }

    @Test
    void 자식댓글삭제() {  //부모 댓글 삭제시 자동으로 답글 삭제 테스트
        CommentRequest entity1 = new CommentRequest();
        entity1.setContent("테스트");
        CommentRequest entity2 = new CommentRequest();
        entity2.setContent("답글");
        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        channelRepository.save(channel);
        Video video = Video.builder()
                .channel(channel)
                .build();
        videoRepository.save(video);
        CommentResponse comment = commentService.write(entity1, channel, video);
        CommentResponse replyComment = commentService.replyWrite(entity2, channel, comment.getId());
        //when
        commentService.commentDelete(replyComment.getId(), channel);
        //then
        assertThat(channel.getComments().size()).isEqualTo(1);
        assertThat(video.getComments().size()).isEqualTo(1);
        List<Comment> result = commentRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void 비디오삭제시댓글삭제() {  //부모 댓글 삭제시 자동으로 답글 삭제 테스트
        CommentRequest entity1 = new CommentRequest();
        entity1.setContent("테스트");
        CommentRequest entity2 = new CommentRequest();
        entity2.setContent("답글");
        Channel channel = Channel.builder()
                .title("Title")
                .description("Description")
                .build();
        Video video = Video.builder()
                .channel(channel)
                .build();
        channelRepository.save(channel);
        videoRepository.save(video);
        CommentResponse comment = commentService.write(entity1, channel, video);
        CommentResponse replyComment = commentService.replyWrite(entity2, channel, comment.getId());
        //when
        videoRepository.delete(video);
        //then
        List<Comment> result = commentRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }

}
