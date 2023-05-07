package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.CommentLike;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentLikeRepository;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class CommentLikeService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentLikeService(CommentRepository commentRepository, CommentLikeRepository commentLikeRepository) {
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    public CommentLikeResponse get(Comment comment, Channel channel) {
        return CommentLikeResponse.builder()
                .commentId(comment.getId())
                .likeCount(comment.getLikeCount())
                .isLike(comment.isLike(channel))
                .build();
    }

    public CommentLikeResponse LikeAdd(Long commentId, Channel channel) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new EntityNotFoundException("comment is not found."));
        CommentLike commentLike = CommentLike.builder()
                .channel(channel)
                .comment(comment)
                .build();
        commentLikeRepository.save(commentLike);
        comment.getLikes().add(commentLike);
        commentRepository.save(comment);
        return this.get(comment, channel);
    }

    public CommentLikeResponse LikeDelete(Long commentId, Channel channel) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new EntityNotFoundException("comment is not found."));
        CommentLike commentLike = commentLikeRepository.findByCommentAndChannel(comment, channel);
        comment.getLikes().remove(commentLike);
        commentRepository.save(comment);
        commentLikeRepository.deleteByCommentAndChannel(comment, channel);
        return this.get(comment, channel);
    }
}
