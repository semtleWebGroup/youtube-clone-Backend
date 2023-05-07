package com.semtleWebGroup.youtubeclone.domain.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    User("ROLE_USER"), NotVerified("ROLE_NotVerifiedUser");

    private String roleName;

    Role(String role) {
        this.roleName = role;
    }
}