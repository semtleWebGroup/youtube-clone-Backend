package com.semtleWebGroup.youtubeclone.domain.comment.api;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.CommentLike;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/comments")
public class CommentApi {
    private final CommentService commentService;
    @Autowired
    public CommentApi(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody CommentRequest dto){
        Comment comment = commentService.write(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity editComment(@PathVariable("commentId")Integer commentId, @RequestBody CommentRequest dto){
        final Optional<Comment> comment = commentService.updateComment(commentId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @DeleteMapping(value= "/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId")Integer commentId){
        commentService.commentDelete(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping(value = "")
    public ResponseEntity list(@RequestParam("videoId")Integer videoId) {
        List<Comment> comments = commentService.getCommentList();
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }
    @PostMapping("/{commentId}/like")
    public ResponseEntity commentLike(@PathVariable("commentId")Integer commentId, @RequestBody CommentLikeRequest Likedto){
        CommentLike commentlike = commentService.like(commentId,Likedto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentlike);
    }
    @DeleteMapping(value= "/{commentLikeId}/like")
    public ResponseEntity commentUnLike(@PathVariable("commentLikeId")Integer commentLikeId){
        commentService.unlike(commentLikeId);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
