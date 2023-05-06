package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
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
        Comment comment2 = commentService.updateComment(comment.getId(),entity2);
        //Then
        Comment findComment = commentRepository.findById(comment2.getId()).get();
        assertEquals(entity2.getContent(), findComment.getContents());
    }

    @Test
    void 목록() {
        CommentRequest entity1 =new CommentRequest();
        entity1.setContent("테스트");
        CommentRequest entity2 =new CommentRequest();
        entity2.setContent("테스트");
//        Video video = new Video(new Channel("title", "description"));
//        UUID id = UUID.randomUUID();
//        video.setVideoId(id);
        Comment comment1 = commentService.write(entity1);
//        comment1.setVideo(video);
        Comment comment2 = commentService.write(entity2);
//        comment2.setVideo(video);
        //when
        List<Comment> CommentList = commentService.getCommentAll(1L);
        //then
        assertThat(CommentList.size()).isEqualTo(2);


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

}