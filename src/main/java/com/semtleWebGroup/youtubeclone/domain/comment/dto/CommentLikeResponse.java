package com.semtleWebGroup.youtubeclone.domain.comment.dto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentLikeResponse {
    private Long commentId;
    private boolean isLike;
    private int likeCount;
    @Builder
    public CommentLikeResponse(Long commentId, boolean isLike, int likeCount) {
        this.commentId = commentId;
        this.isLike = isLike;
        this.likeCount = likeCount;
    }

}
