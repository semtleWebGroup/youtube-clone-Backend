package com.semtleWebGroup.youtubeclone.domain.comment.service;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.*;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
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
    @Transactional
    public CommentResponse write(CommentRequest dto, Channel channel, Video video){
        Comment newComment = Comment.builder()
                .contents(dto.getContent())
                .video(video)
                .channel(channel)
                .build();
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
                .video(video)
                .channel(channel)
                .rootComment(rootComment)
                .build();
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
        Page<Comment> commentList = commentRepository.findByVideo_Id(video_Idx, pageable);
        return new CommentPageResponse(commentList, channel);
    }

    public CommentReplyPageResponse getReplyList(Long comment_Idx, Channel channel, Pageable pageable){
        Page<Comment> commentList = commentRepository.findByRootComment_Id(comment_Idx, pageable);
        return new CommentReplyPageResponse(commentList,channel);
    }
    @Transactional
    public void commentDelete(Long idx){
        Comment entity = commentRepository.findById(idx).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", idx)
        ));
        commentRepository.delete(entity);
    }
}
