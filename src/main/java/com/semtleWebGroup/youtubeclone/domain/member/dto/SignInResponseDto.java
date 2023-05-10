package com.semtleWebGroup.youtubeclone.domain.member.dto;

import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

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
    
    private Set<String> channelTokens=new LinkedHashSet<>();
    
    @Builder
    public SignInResponseDto(boolean success, int code, String msg, String memberToken,Set<String> channelTokens) {
        super(success, code, msg);
        this.memberToken = memberToken;
        this.channelTokens=channelTokens;
    }
}