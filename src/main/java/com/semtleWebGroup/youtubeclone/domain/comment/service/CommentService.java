package com.semtleWebGroup.youtubeclone.domain.comment.service;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.*;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.dto.VideoListResponse;
import com.semtleWebGroup.youtubeclone.global.error.FieldError;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    @Transactional
    public CommentResponse write(CommentRequest dto, Channel channel, Video video){
        Comment newComment = Comment.builder()
                .contents(dto.getContent())
                .build();
        channel.addComment(newComment);  //채널에 댓글 정보 입력 , 댓글에도 채널 정보 입력 
        video.addComment(newComment);   //비디오에 댓글 정보 엽력 , 댓글에도 비디오 정보 입력
        commentRepository.save(newComment);
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
        channel.addComment(newComment);  //채널에 댓글 정보 입력 , 댓글에도 채널 정보 입력
        video.addComment(newComment);   //비디오에 댓글 정보 엽력 , 댓글에도 비디오 정보 입력
        rootComment.addReplyComment(newComment);
        commentRepository.save(newComment);
        return new CommentResponse(newComment);
    }
    @Transactional
    public CommentResponse updateComment(Long idx, CommentRequest dto) {
        Comment entity = commentRepository.findById(idx).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", idx)
        ));
        entity.update(dto.getContent());
        commentRepository.save(entity);
        return new CommentResponse(entity);
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
    public void commentDelete(Long idx){
        Comment comment = commentRepository.findById(idx).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", idx)
        ));
        Channel channel = comment.getChannel();
        Video video = comment.getVideo();
        channel.deleteComment(comment);  //채널에서 댓글 정보 삭제
        video.deleteComment(comment);   //비디오에서 댓글 정보 삭제
        if(comment.getRootComment() != null){
            Comment rootComment = comment.getRootComment();
            rootComment.deleteReplyComment(comment); //부모 댓글에서 댓글 정보 삭제
        }
        commentRepository.delete(comment);
    }

}
