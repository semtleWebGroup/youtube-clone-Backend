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
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentLikeResponse get(Comment comment, Channel channel) {
        return CommentLikeResponse.builder()
                .commentId(comment.getId())
                .likeCount(comment.getLikeCount())
                .isLike(comment.isLike(channel))
                .build();
    }
    @Transactional
    public CommentLikeResponse likeAdd(Long commentId, Channel channel) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new EntityNotFoundException("comment is not found."));
        CommentLike commentLike = CommentLike.builder()
                .channel(channel)          //좋아요에 채널 정보 추가
                .build();
        channel.likeComment(comment);     //채널에 좋아요한 댓글 추가
        comment.likeComment(commentLike);  //댓글에 좋아요 정보 추가 , 좋아요에도 댓글 정보 추가
        commentLikeRepository.save(commentLike);
        return this.get(comment, channel);
    }
    @Transactional
    public CommentLikeResponse likeDelete(Long commentId, Channel channel) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new EntityNotFoundException("comment is not found."));
        CommentLike commentLike = commentLikeRepository.findByCommentAndChannel(comment, channel);
        comment.unLikeComment(commentLike);  //댓글에서 좋아요 정보 삭제
        channel.unLikeComment(comment);    //채널에서 좋아요 정보 삭제
        commentLikeRepository.deleteByCommentAndChannel(comment, channel);   //좋아요 정보 삭제
        return this.get(comment, channel);
    }
}
