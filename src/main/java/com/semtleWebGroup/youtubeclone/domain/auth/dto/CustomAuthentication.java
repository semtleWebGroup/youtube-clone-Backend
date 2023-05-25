package com.semtleWebGroup.youtubeclone.domain.auth.dto;

import com.semtleWebGroup.youtubeclone.domain.auth.token.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomAuthentication implements Authentication {
    private final Map<AccessToken.Field, String> map;
    private boolean authenticated = true;

    public CustomAuthentication(Map<AccessToken.Field, String> map) {
        this.map = map;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> collect = Stream.of(map.get(AccessToken.Field.ROLE))
                .map(r -> new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return r;
                    }
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public Object getDetails() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public Object getPrincipal() {
        return TokenInfo.of(map);
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return null;
    }
}
