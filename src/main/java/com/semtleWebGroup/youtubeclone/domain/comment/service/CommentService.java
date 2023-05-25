package com.semtleWebGroup.youtubeclone.domain.comment.service;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.*;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.exception.ForbiddenException;
import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.FieldError;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private void checkIdentification(Comment comment, Channel channel) {
        if (channel == null || !comment.getChannel().getId().equals(channel.getId()))
            throw new ForbiddenException(ErrorCode.ACCESS_DENIED);
    }
    @Transactional
    public CommentResponse write(CommentRequest dto, Channel channel, Video video){
        Comment newComment = Comment.builder()
                .contents(dto.getContent())
                .build();
        commentRepository.save(newComment);  //댓글은 부모가 2개여서 따로 저장
        channel.addComment(newComment);  //채널에 댓글 정보 입력 , 댓글에도 채널 정보 입력
        video.addComment(newComment);   //비디오에 댓글 정보 엽력 , 댓글에도 비디오 정보 입력
        return new CommentResponse(newComment);
    }
    @Transactional
    public CommentResponse replyWrite(CommentRequest dto, Channel channel, Long rootCommentId){
        Comment rootComment = commentRepository.findById(rootCommentId).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", rootCommentId)
        ));
        if (rootComment.getRootComment() != null)
        {
            String rootCommentIdString = rootCommentId.toString();
            throw new BadRequestException(FieldError.of("rootComment", rootCommentIdString, "Cannot reply to replies"));
        }
        Video video = rootComment.getVideo();
        Comment newComment = Comment.builder()
                .contents(dto.getContent())
                .build();
        commentRepository.save(newComment);  //댓글은 부모가 2개여서 따로 저장
        channel.addComment(newComment);  //채널에 댓글 정보 입력 , 댓글에도 채널 정보 입력
        video.addComment(newComment);   //비디오에 댓글 정보 엽력 , 댓글에도 비디오 정보 입력
        rootComment.addReplyComment(newComment);
        return new CommentResponse(newComment);
    }
    @Transactional
    public CommentResponse updateComment(Long idx, CommentRequest dto, Channel channelLogin) {
        Comment comment = commentRepository.findById(idx).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", idx)
        ));
        checkIdentification(comment, channelLogin);  //권한확인
        comment.update(dto.getContent());
        commentRepository.save(comment);  //댓글은 부모가 2개여서 따로 저장
        Video video = comment.getVideo();
        Channel channel = comment.getChannel();
        for (Comment videoComment: video.getComments()) {  //비디오에 저장된 값도 업데이트
            if (videoComment.getId().equals(comment.getId()))
                videoComment.update(dto.getContent());
        }
        for (Comment channelComment: channel.getComments()) {  //채널에 저장된 값도 업데이트
            if (channelComment.getId().equals(comment.getId()))
                channelComment.update(dto.getContent());
        }
        return new CommentResponse(comment);
    }

    public CommentPageResponse getCommentList(UUID video_Idx, Channel channel, Pageable pageable){
        Page<Comment> commentList = commentRepository.findByVideo_IdAndRootComment_Id(video_Idx,null, pageable);
        return new CommentPageResponse(commentList, channel);
    }

    public CommentPageResponse getReplyList(Long comment_Idx, Channel channel, Pageable pageable){
        Page<Comment> commentList = commentRepository.findByRootComment_Id(comment_Idx, pageable);
        return new CommentPageResponse(commentList,channel);
    }
    @Transactional
    public void commentDelete(Long idx, Channel channelLogin){
        Comment comment = commentRepository.findById(idx).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", idx)
        ));
        checkIdentification(comment, channelLogin);  //권한확인
        Channel channel = comment.getChannel();
        Video video = comment.getVideo();
        channel.deleteComment(comment);  //채널에서 댓글 정보 삭제
        video.deleteComment(comment);   //비디오에서 댓글 정보 삭제
        for (Comment replyComment: comment.getReplyComments()) {  //댓글에서 자식들 정보 삭제
            comment.deleteReplyComment(replyComment);
        }
        commentRepository.delete(comment);  //댓글은 부모가 많아서 따로 삭제
    }

}