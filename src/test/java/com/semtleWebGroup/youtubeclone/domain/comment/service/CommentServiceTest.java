package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.CommentLike;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentLikeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentLikeRepository commentLikeRepository;
    @Test
    void 등록() {
        CommentRequest entity =new CommentRequest();
        entity.setContent("테스트");
        //When
        Comment comment = commentService.write(entity);
        //Then
        Comment findComment = commentRepository.findById(comment.getId()).get();
        assertEquals(entity.getContent(), findComment.getContents());
    }

    @Test
    void 업데이트() {
        CommentRequest entity =new CommentRequest();
        entity.setContent("테스트");
        Comment comment=commentService.write(entity);

        CommentRequest entity2 =new CommentRequest();
        entity2.setContent("업데이트");
        //When
        Optional<Comment> comment2 = commentService.updateComment(comment.getId(),entity2);
        //Then
        Comment findComment = commentRepository.findById(comment2.get().getId()).get();
        assertEquals(entity2.getContent(), findComment.getContents());
    }

    @Test
    void 목록() {
        CommentRequest entity1 =new CommentRequest();
        entity1.setContent("테스트");
        CommentRequest entity2 =new CommentRequest();
        entity2.setContent("테스트");
        Comment comment1 = commentService.write(entity1);
        Comment comment2 = commentService.write(entity2);
        //when
        List<Comment> result = commentRepository.findAll();
        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void 삭제() {
        CommentRequest entity =new CommentRequest();
        entity.setContent("테스트");
        Comment comment = commentService.write(entity);
        //when
        commentService.commentDelete(comment.getId());
        //then
        List<Comment> result = commentRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void 좋아요등록() {
        CommentLikeRequest entity =new CommentLikeRequest();
        entity.setChannelId(1000);
        //When
        CommentLike commentLike = commentService.like(1,entity);
        //Then
        CommentLike findCommentLike = commentLikeRepository.findById(commentLike.getId()).get();
        assertEquals(1, findCommentLike.getCommentId());
        assertEquals(entity.getChannelId(), findCommentLike.getChannelId());
    }

    @Test
    void 좋아요취소() {
        CommentLikeRequest entity =new CommentLikeRequest();
        entity.setChannelId(1000);
        CommentLike commentLike = commentService.like(1,entity);
        //when
        commentService.unlike(commentLike.getId());
        //then
        List<CommentLike> result = commentLikeRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }
}