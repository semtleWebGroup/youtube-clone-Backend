package com.semtleWebGroup.youtubeclone.domain.member.dto;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link Member} entity
 */
@Data
public class MemberDto implements Serializable {
    private final Integer id;
    @Size(max = 45)
    @NotNull
    private final String email;
    @Size(max = 15)
    @NotNull
    private final String nickname;
    @Size(max = 20)
    @NotNull
    private final String password;

    public Member toEntity() {
        Member member = new Member();
        member.setId(this.id);
        member.setEmail(this.email);
        member.setNickname(this.nickname);
        member.setPassword(this.password);
        return member;
    }

}