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
public class CommentReplyPageResponse {
    private int totalPages;
    private int pageNumber;
    private int numberOfElements;
    private List<CommentViewResponse> comments = new ArrayList<>();

    public CommentReplyPageResponse(Page<Comment> comment, Channel channel) {
        this.totalPages = comment.getTotalPages();
        this.pageNumber = comment.getNumber();
        this.numberOfElements = comment.getNumberOfElements();
        this.comments = getCommentViewList(comment.getContent(), channel);
    }
    public List<CommentViewResponse> getCommentViewList(List<Comment> comments, Channel channel) {
        List<CommentViewResponse> tempComments = new ArrayList<>();
        ListIterator<Comment> iterator = comments.listIterator();
        while(iterator.hasNext()){
            Comment comment = iterator.next();
            CommentViewResponse commentViewResponse = CommentViewResponse.builder()
                    .comment(comment)
                    .isLike(comment.isLike(channel))
                    .build();
            tempComments.add(commentViewResponse);
        }
        return tempComments;
    }
}
