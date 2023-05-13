package com.semtleWebGroup.youtubeclone.domain.comment.service;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentViewResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.repository.CommentRepository;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;


@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment write(CommentRequest dto, Channel channel, Video video){
        Comment newComment = Comment.builder()
                .contents(dto.getContent())
                .video(video)
                .channel(channel)
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
            CommentViewResponse commentViewResponse = CommentViewResponse.builder()
                    .comment(comment)
                    .isLike(comment.isLike(channel))
                    .build();
            commentViewResponseList.add(commentViewResponse);
        }
        return commentViewResponseList;
    }
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
