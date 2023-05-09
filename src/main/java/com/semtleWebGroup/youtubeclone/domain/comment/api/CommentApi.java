package com.semtleWebGroup.youtubeclone.domain.comment.api;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentLikeService;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/comments")
public class CommentApi {
    private final CommentService commentService;

    private final CommentLikeService commentLikeService;
    public CommentApi(CommentService commentService,  CommentLikeService commentLikeService) {
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
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
    @PostMapping("/{commentId}/like")
    public ResponseEntity like(@PathVariable("commentId")Long commentId, @RequestPart Channel channel) {
        CommentLikeResponse commentLikeResponse = commentLikeService.LikeAdd(commentId, channel);

        return ResponseEntity.status(HttpStatus.OK).body(commentLikeResponse);
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity unlike(@PathVariable("commentId")Long commentId, @RequestPart Channel channel) {
        System.out.println("실행됨");
        System.out.println(channel);
        CommentLikeResponse commentLikeResponse = commentLikeService.LikeDelete(commentId, channel);

        return ResponseEntity.status(HttpStatus.OK).body(commentLikeResponse);
    }

}
