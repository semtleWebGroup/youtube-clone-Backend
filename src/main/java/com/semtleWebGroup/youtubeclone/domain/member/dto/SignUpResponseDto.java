package com.semtleWebGroup.youtubeclone.domain.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpResponseDto {
    private Boolean success;
    private Integer code;
    private String msg;
}