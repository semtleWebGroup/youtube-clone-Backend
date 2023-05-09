package com.semtleWebGroup.youtubeclone.domain.comment.service;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment write(CommentRequest dto){
        Comment newComment = Comment.builder()
                .contents(dto.getContent())
                .build();

        return commentRepository.save(newComment);
    }

    public Comment updateComment(Long idx, CommentRequest dto) {
        Comment entity = commentRepository.findById(idx).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", idx)
        ));
        entity.update(dto.getContent());
        commentRepository.save(entity);
        return entity;
    }

//    public List<Comment> getCommentList(UUID Idx){
//        List<Comment> commentList = commentRepository.findByVideo_VideoId(Idx);  //실패..
//        return commentList;
//    }
    public List<Comment> getCommentAll(Long Idx){
        return commentRepository.findAll();
    }

    public void commentDelete(Long idx){
        Comment entity = commentRepository.findById(idx).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", idx)
        ));
        commentRepository.delete(entity);
    }


}
