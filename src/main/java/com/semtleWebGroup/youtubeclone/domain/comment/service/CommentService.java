package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.CommentLike;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentLikeRespository;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikeRespository commentLikeRespository;
    @Autowired
    public CommentService(CommentRepository commentRepository,CommentLikeRespository commentLikeRespository) {
        this.commentRepository = commentRepository;
        this.commentLikeRespository = commentLikeRespository;
    }

    public Comment write(CommentRequest dto){
        Comment newComment = Comment.builder()
                .contents(dto.getContent())
                .build();

        commentRepository.save(newComment);
        return newComment;
    }

    public Optional<Comment> updateComment(Integer idx, CommentRequest dto) {
        Optional<Comment> entity = Optional.ofNullable(commentRepository.findById(idx).orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다.")));
        entity.ifPresent(t ->{
            if(dto.getContent() != null) {
                t.setContents(dto.getContent());
                t.setUpdatedTime(LocalDateTime.now());
            }
            commentRepository.save(t);
        });
        return entity;
    }

    public List<Comment> getCommentList(){
        return commentRepository.findAll();
    }

    public void commentDelete(Integer id){
        commentRepository.deleteById(id);
    }

    public CommentLike like(Integer commentId,CommentLikeRequest likeDto){
        CommentLike newCommentLike = CommentLike.builder()
                .channelId(likeDto.getChannelId())
                .build();
        newCommentLike.setCommentId(commentId);
        commentLikeRespository.save(newCommentLike);
        return newCommentLike;
    }
    public void unlike(Integer id){
        commentLikeRespository.deleteById(id);
    }

}
