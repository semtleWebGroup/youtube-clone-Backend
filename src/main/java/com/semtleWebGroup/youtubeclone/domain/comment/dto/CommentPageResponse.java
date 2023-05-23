package com.semtleWebGroup.youtubeclone.domain.comment.dto;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.comment.domain.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentPageResponse {
    private int totalPages;
    private int pageNumber;
    private int numberOfElements;
    private List<CommentViewResponse> comments = new ArrayList<>();

    public CommentPageResponse(Page<Comment> comments, Channel channel) {
        this.totalPages = comments.getTotalPages();
        this.pageNumber = comments.getNumber();
        this.numberOfElements = comments.getNumberOfElements();
        for (Comment comment: comments.getContent()) {
            CommentViewResponse commentViewResponse = new CommentViewResponse(comment,comment.isLike(channel));
            this.comments.add(commentViewResponse);
        }
    }
}
