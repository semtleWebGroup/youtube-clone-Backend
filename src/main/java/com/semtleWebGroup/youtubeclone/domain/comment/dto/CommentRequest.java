package com.semtleWebGroup.youtubeclone.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentRequest {
    //@Size(max=45, message="'content' length should be <= 45.")
    private String content;

//    private Integer videoPageId;
//
//    private Integer channelId;
//
//    private Integer commentCommentId;

}
