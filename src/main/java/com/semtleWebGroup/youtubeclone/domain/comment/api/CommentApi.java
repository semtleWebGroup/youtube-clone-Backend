package com.semtleWebGroup.youtubeclone.domain.comment.api;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentViewResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentLikeService;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentService;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentApi {
    private final CommentService commentService;
    private final VideoService videoService; //임시사용
    private final CommentLikeService commentLikeService;

    @PostMapping("/{videoId}")
    public ResponseEntity create(@RequestPart CommentRequest dto,  @RequestPart Channel channel,  @PathVariable UUID videoId){
        Video video = videoService.getVideo(videoId);
        Comment comment = commentService.write(dto, channel , video);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity editComment(@PathVariable("commentId")Long commentId, @RequestBody CommentRequest dto){
        Comment comment = commentService.updateComment(commentId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId")Long commentId){
        commentService.commentDelete(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("")
    public ResponseEntity list(@RequestParam("videoId") UUID videoId, @RequestPart Channel channel) {
        List<CommentViewResponse> CommentList = commentService.getCommentList(videoId, channel);
        return ResponseEntity.status(HttpStatus.OK).body(CommentList);
    }
    @GetMapping("/reply")
    public ResponseEntity replyList(@RequestParam("commentId") Long commentId, @RequestPart Channel channel) {
        List<CommentViewResponse> CommentList = commentService.getReplyList(commentId, channel);
        return ResponseEntity.status(HttpStatus.OK).body(CommentList);
    }
    @PostMapping("/{commentId}/like")
    public ResponseEntity like(@PathVariable("commentId")Long commentId, @RequestPart Channel channel) {
        CommentLikeResponse commentLikeResponse = commentLikeService.likeAdd(commentId, channel);
        
        return ResponseEntity.status(HttpStatus.OK).body(commentLikeResponse);
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity unlike(@PathVariable("commentId")Long commentId, @RequestPart Channel channel) {
        CommentLikeResponse commentLikeResponse = commentLikeService.likeDelete(commentId, channel);

        return ResponseEntity.status(HttpStatus.OK).body(commentLikeResponse);
    }
    @PostMapping("/{videoId}/{commentId}")
    public ResponseEntity replyCreate(@PathVariable("videoId")UUID videoId,  @PathVariable("commentId")Long rootCommentId ,@RequestPart CommentRequest dto,  @RequestPart Channel channel){
        Video video = videoService.getVideo(videoId);
        Comment comment = commentService.replyWrite(dto, channel , video, rootCommentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
}
