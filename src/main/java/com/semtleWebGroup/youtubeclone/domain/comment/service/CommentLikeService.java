package com.semtleWebGroup.youtubeclone.domain.comment.service;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.CommentLike;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentLikeRepository;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentLikeResponse get(Comment comment, Channel channel) {
        return CommentLikeResponse.builder()   //스태틱 팩토리 메소드 공부해서 바꿔보기
                .commentId(comment.getId())
                .likeCount(comment.getLikeCount())
                .isLike(comment.isLike(channel))
                .build();
    }

    public CommentLikeResponse likeAdd(Long commentId, Channel channel) {
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

    public CommentLikeResponse likeDelete(Long commentId, Channel channel) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new EntityNotFoundException("comment is not found."));
        CommentLike commentLike = commentLikeRepository.findByCommentAndChannel(comment, channel);
        comment.getLikes().remove(commentLike);
        commentRepository.save(comment);
        commentLikeRepository.deleteByCommentAndChannel(comment, channel);
        return this.get(comment, channel);
    }
}
