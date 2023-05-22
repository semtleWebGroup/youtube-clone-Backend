package com.semtleWebGroup.youtubeclone.domain.comment.api;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentLikeResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentRequest;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.CommentViewResponse;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentLikeService;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentService;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentApi {
    private final CommentService commentService;
    private final VideoService videoService;
    private final CommentLikeService commentLikeService;

    @PostMapping("/{videoId}")
    public ResponseEntity create(@RequestPart CommentRequest dto,  @RequestPart Channel channel,  @PathVariable UUID videoId){
        Video video = videoService.getVideo(videoId);
        CommentResponse comment = commentService.write(dto, channel , video);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PostMapping("/reply/{commentId}")
    public ResponseEntity replyCreate(@PathVariable("commentId")Long rootCommentId ,@RequestPart CommentRequest dto,  @RequestPart Channel channel){
        CommentResponse comment = commentService.replyWrite(dto, channel , rootCommentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity editComment(@PathVariable("commentId")Long commentId, @RequestBody CommentRequest dto){
        CommentResponse comment = commentService.updateComment(commentId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId")Long commentId){
        commentService.commentDelete(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("")
    public ResponseEntity list(@RequestParam("videoId") UUID videoId, @RequestPart Channel channel, Pageable pageable) {
        List<CommentViewResponse> CommentList = commentService.getCommentList(videoId, channel, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(CommentList);
    }
    @GetMapping("/reply")
    public ResponseEntity replyList(@RequestParam("commentId") Long commentId, @RequestPart Channel channel, Pageable pageable) {
        List<CommentViewResponse> CommentList = commentService.getReplyList(commentId, channel, pageable);
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
}
