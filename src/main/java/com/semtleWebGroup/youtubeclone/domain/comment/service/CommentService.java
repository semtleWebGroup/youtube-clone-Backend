package com.semtleWebGroup.youtubeclone.domain.comment.service;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentViewResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public Comment write(CommentRequest dto, Channel channel, Video video){
        Comment newComment = Comment.builder()
                .contents(dto.getContent())
                .video(video)
                .channel(channel)
                .build();

        return commentRepository.save(newComment);
    }

    public Comment replyWrite(CommentRequest dto, Channel channel, Video video, Long rootCommentId){
        Comment rootComment = commentRepository.findById(rootCommentId).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", rootCommentId)
        ));

        Comment newComment = Comment.builder()
                .contents(dto.getContent())
                .video(video)
                .channel(channel)
                .rootComment(rootComment)
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

    public List<CommentViewResponse> getCommentList(UUID video_Idx, Channel channel){
        List<Comment> commentList = commentRepository.findByVideo_Id(video_Idx);
        List<CommentViewResponse>  commentViewResponseList = new ArrayList<>();
        ListIterator<Comment> iterator = commentList.listIterator();
        while(iterator.hasNext()){
            Comment comment = iterator.next();
            if(comment.getRootComment() != null){
                continue;
            }
            CommentViewResponse commentViewResponse = CommentViewResponse.builder()
                    .comment(comment)
                    .isLike(comment.isLike(channel))
                    .build();
            commentViewResponseList.add(commentViewResponse);
        }
        return commentViewResponseList;
    }

    public List<CommentViewResponse> getReplyList(Long comment_Idx, Channel channel){
        List<Comment> commentList = commentRepository.findByRootComment_Id(comment_Idx);
        List<CommentViewResponse>  commentViewResponseList = new ArrayList<>();
        ListIterator<Comment> iterator = commentList.listIterator();
        while(iterator.hasNext()){
            Comment comment = iterator.next();
            CommentViewResponse commentViewResponse = CommentViewResponse.builder()
                    .comment(comment)
                    .isLike(comment.isLike(channel))
                    .build();
            commentViewResponseList.add(commentViewResponse);
        }
        return commentViewResponseList;
    }

    public void commentDelete(Long idx){
        Comment entity = commentRepository.findById(idx).orElseThrow(()->new EntityNotFoundException(
                String.format("%d is not found.", idx)
        ));
        commentRepository.delete(entity);
    }

}
