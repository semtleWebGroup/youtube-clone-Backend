package com.semtleWebGroup.youtubeclone.domain.member.dto;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * package :  com.semtleWebGroup.youtubeclone.domain.member.dto
 * fileName : SigninDto
 * author :  ShinYeaChan
 * date : 2023-05-07
 */
@Data
@NoArgsConstructor
public class SignInRequestDto implements Serializable {
    @Size(max = 45)
    @NotNull
    private String email;
    @Size(max = 60)
    @NotNull
    private String password;
    private Role role;
}
