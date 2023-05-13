package com.semtleWebGroup.youtubeclone.domain.comment.dto;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentViewResponse {
    //comment 정보
    private Long commentId;
    private String contents;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    //commentLike 정보
    private int likeCount;
    private boolean isLike;
    //comment 작성자의 정보
    private Long channelId;
    private String channelName;
    private byte[] channelProfileImg;

    @Builder
    public CommentViewResponse(Comment comment, Boolean isLike) {
        //comment 정보
        this.commentId = comment.getId();
        this.contents = comment.getContents();
        this.createdTime = comment.getCreatedTime();
        this.updatedTime = comment.getUpdatedTime();
        //commentLike -> 현재 유튜브 사용자가 보는 comment 정보
        this.likeCount = comment.getLikeCount();
        this.isLike = isLike;
        //comment 작성자의 정보
        this.channelId = comment.getChannel().getId();
        this.channelName = comment.getChannel().getTitle();
        this.channelProfileImg = convertBlobToBytes(comment.getChannel().getChannelImage());
    }
    private byte[] convertBlobToBytes (Blob blob) {
        try {
            if (blob != null) blob.getBytes(1, (int) blob.length());
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO: Handle Exception
        }
        return null;
    }
}
