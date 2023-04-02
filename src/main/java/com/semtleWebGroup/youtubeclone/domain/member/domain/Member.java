package com.semtleWebGroup.youtubeclone.domain.member.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "memberid", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Size(max = 15)
    @NotNull
    @Column(name = "nickname", nullable = false, length = 15)
    private String nickname;

    @Size(max = 20)
    @NotNull
    @Column(name = "password", nullable = false, length = 20)
    private String password;

}