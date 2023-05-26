package com.semtleWebGroup.youtubeclone.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormBody{
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
