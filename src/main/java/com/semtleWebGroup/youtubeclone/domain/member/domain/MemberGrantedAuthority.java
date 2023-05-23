package com.semtleWebGroup.youtubeclone.domain.member.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.semtleWebGroup.youtubeclone.domain.member.config.MemberGrantedAuthorityDeserializer;
import org.springframework.security.core.GrantedAuthority;

/**
 * package :  com.semtleWebGroup.youtubeclone.domain.member.domain
 * fileName : MemberGrantedAuthority
 * author :  ShinYeaChan
 * date : 2023-05-12
 */
@JsonDeserialize(using = MemberGrantedAuthorityDeserializer.class)
public class MemberGrantedAuthority implements GrantedAuthority {
    public MemberGrantedAuthority(String authority) {
        this.authority = authority;
    }
    private String authority;
    @Override
    public String getAuthority() {
        return authority;
    }
}
