package com.semtleWebGroup.youtubeclone.domain.member.dto;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.semtleWebGroup.youtubeclone.domain.member.domain.Member} entity
 */
@Data
public class SignUpRequestDto implements Serializable {
    @Email
    @Size(max = 45)
    @NotNull
    private final String email;
    @Size(max = 15)
    @NotNull
    private final String name;
    @Size(max = 60)
    @NotNull
    private final String password;
    private final Role role;
}