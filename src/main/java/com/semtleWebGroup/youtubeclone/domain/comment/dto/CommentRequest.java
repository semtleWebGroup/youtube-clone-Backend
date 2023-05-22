package com.semtleWebGroup.youtubeclone.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class CommentRequest {
    @Size(max=45, message="'content' length should be <= 45.")
    private String content;


}
