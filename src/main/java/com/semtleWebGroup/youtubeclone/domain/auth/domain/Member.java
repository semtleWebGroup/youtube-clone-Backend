package com.semtleWebGroup.youtubeclone.domain.auth.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email"},name = "member_email_unique"))
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    Long id;

    @NotNull
    @Email
    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "password",nullable = false)
    String password;

    @Column(name = "role",nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private List<Channel> channels = new ArrayList<>();


    public Member(String email, String password, Role role, List<Channel> channels) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.channels = channels;
    }
}
