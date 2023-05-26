package com.semtleWebGroup.youtubeclone.domain.comment.api;

import com.semtleWebGroup.youtubeclone.domain.auth.dto.TokenInfo;
import com.semtleWebGroup.youtubeclone.domain.channel.application.ChannelService;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.dto.*;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentLikeService;
import com.semtleWebGroup.youtubeclone.domain.comment.service.CommentService;
import com.semtleWebGroup.youtubeclone.domain.video.domain.Video;
import com.semtleWebGroup.youtubeclone.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentApi {
    private final CommentService commentService;
    private final VideoService videoService;
    private final ChannelService channelService;
    private final CommentLikeService commentLikeService;

    @PostMapping("/{videoId}")
    public ResponseEntity create(@PathVariable UUID videoId, @Valid @RequestPart CommentRequest dto, @AuthenticationPrincipal TokenInfo principal){
        Video video = videoService.getVideo(videoId);
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);
        CommentResponse comment = commentService.write(dto, channel , video);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PostMapping("/reply/{commentId}")
    public ResponseEntity replyCreate(@PathVariable("commentId")Long rootCommentId ,@Valid @RequestPart CommentRequest dto, @AuthenticationPrincipal TokenInfo principal){
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);
        CommentResponse comment = commentService.replyWrite(dto, channel , rootCommentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity editComment(@PathVariable("commentId")Long commentId, @Valid @RequestBody CommentRequest dto, @AuthenticationPrincipal TokenInfo principal){
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);
        CommentResponse comment = commentService.updateComment(commentId, dto, channel);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId")Long commentId, @AuthenticationPrincipal TokenInfo principal){
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);
        commentService.commentDelete(commentId, channel);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("")
    public ResponseEntity list(@RequestParam("videoId") UUID videoId, @AuthenticationPrincipal TokenInfo principal, Pageable pageable) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);
        CommentPageResponse CommentList = commentService.getCommentList(videoId, channel, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(CommentList);
    }
    @GetMapping("/reply")
    public ResponseEntity replyList(@RequestParam("commentId") Long commentId, @AuthenticationPrincipal TokenInfo principal, Pageable pageable) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);
        CommentPageResponse CommentList = commentService.getReplyList(commentId, channel, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(CommentList);
    }
    @PostMapping("/{commentId}/like")
    public ResponseEntity like(@PathVariable("commentId")Long commentId, @AuthenticationPrincipal TokenInfo principal) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);
        CommentLikeResponse commentLikeResponse = commentLikeService.like(commentId, channel);
        return ResponseEntity.status(HttpStatus.OK).body(commentLikeResponse);
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity unlike(@PathVariable("commentId")Long commentId, @AuthenticationPrincipal TokenInfo principal) {
        Long channelId = principal.getChannelId();
        Channel channel = channelService.getChannelEntity(channelId);
        CommentLikeResponse commentLikeResponse = commentLikeService.unlike(commentId, channel);
        return ResponseEntity.status(HttpStatus.OK).body(commentLikeResponse);
    }
}