package com.semtleWebGroup.youtubeclone.domain.member.dto;

import lombok.*;

/**
 * package :  com.semtleWebGroup.youtubeclone.domain.member.dto
 * fileName : SignInResultDto
 * author :  ShinYeaChan
 * date : 2023-05-05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResponseDto extends SignUpResponseDto {
    
    private String memberToken;
    
    @Builder
    public SignInResponseDto(boolean success, int code, String msg, String memberToken) {
        super(success, code, msg);
        this.memberToken = memberToken;
    }
}