package com.semtleWebGroup.youtubeclone.domain.comment.dto;

import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {
    private Long id;
    private String contents;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Long rootCommentId;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.createdTime = comment.getCreatedTime();
        this.updatedTime = comment.getUpdatedTime();
        this.rootCommentId = getRootCommentId(comment.getRootComment());
    }
    public Long getRootCommentId(Comment rootComment) {
        if (rootComment != null) {
            return rootComment.getId();
        }
        return null;
    }

}
