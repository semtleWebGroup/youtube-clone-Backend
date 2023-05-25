package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentRepository commentRepository;
    public CommentLikeResponse get(Comment comment, Channel channel) {
        return CommentLikeResponse.builder()
                .commentId(comment.getId())
                .likeCount(comment.getLikeCount())
                .isLike(comment.isLike(channel))
                .build();
    }
    public CommentLikeResponse like(Long commentId, Channel channel) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", commentId)
        ));
        channel.likeComment(comment);
        commentRepository.save(comment);
        return this.get(comment, channel);
    }

    public CommentLikeResponse unlike(Long commentId, Channel channel) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", commentId)
        ));
        channel.unLikeComment(comment); // channel의 like set에서 video 제거 & video의 like set에서 channel 제거
        commentRepository.save(comment);
        return this.get(comment, channel);
    }
}