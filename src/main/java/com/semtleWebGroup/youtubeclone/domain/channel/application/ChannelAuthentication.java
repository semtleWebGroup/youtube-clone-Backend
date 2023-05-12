package com.semtleWebGroup.youtubeclone.domain.channel.application;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class ChannelAuthentication implements Authentication {
    
    private final Channel channel;
    private boolean isAuthenticated;
    
    public ChannelAuthentication(Channel channel) {
        this.channel = channel;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //채널에 Role이 없음
        return null;
    }
    
    @Override
    public Object getCredentials() {
        //채널에 비밀번호 없음
        return null;
    }
    
    @Override
    public Object getDetails() {
        return channel;
    }
    
    @Override
    public Object getPrincipal() {
        return channel.getId();
    }
    
    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }
    
    @Override
    public String getName() {
        return channel.getTitle();
    }
    
}
