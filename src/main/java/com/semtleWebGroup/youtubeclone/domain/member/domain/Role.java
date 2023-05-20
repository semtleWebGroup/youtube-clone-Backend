package com.semtleWebGroup.youtubeclone.domain.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    User("ROLE_MEMBER"), NotVerified("ROLE_NOT_VERIFIED_MEMBER");

    private String roleName;

    Role(String role) {
        this.roleName = role;
    }
}