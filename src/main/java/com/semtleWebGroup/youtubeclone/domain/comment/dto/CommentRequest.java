package com.semtleWebGroup.youtubeclone.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Valid
public class CommentRequest {
    @NotBlank(message = "'content' should be NotBlank")
    @Size(max=45, message="'content' length should be <= 45.")
    private String content;


}
