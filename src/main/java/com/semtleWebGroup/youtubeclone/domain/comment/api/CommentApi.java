package com.semtleWebGroup.youtubeclone.domain.comment.api;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/comments")
public class CommentApi {
    private final CommentService commentService;

    public CommentApi(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody CommentRequest dto){
        Comment comment = commentService.write(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity editComment(@PathVariable("commentId")Long commentId, @RequestBody CommentRequest dto){
        final Comment comment = commentService.updateComment(commentId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId")Long commentId){
        commentService.commentDelete(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("")
    public ResponseEntity list(@RequestParam("videoId") Long videoId) {
//      List<Comment> CommentList = commentService.getCommentList(videoId);
        List<Comment> CommentList = commentService.getCommentAll(videoId);
        return ResponseEntity.status(HttpStatus.OK).body(CommentList);
    }
}
