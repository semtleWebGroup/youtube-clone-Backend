package com.semtleWebGroup.youtubeclone.domain.member.dto;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.semtleWebGroup.youtubeclone.domain.member.domain.Member} entity
 */
@Data
@NoArgsConstructor
public class SignUpRequestDto implements Serializable {
    @Size(max = 45)
    @NotNull
    private String email;
    @Size(max = 15)
    @NotNull
    private String name;
    @Size(max = 60)
    @NotNull
    private String password;
    private Role role;
}