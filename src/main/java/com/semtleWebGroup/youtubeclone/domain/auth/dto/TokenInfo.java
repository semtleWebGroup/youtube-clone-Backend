package com.semtleWebGroup.youtubeclone.domain.auth.dto;

import com.semtleWebGroup.youtubeclone.domain.auth.domain.Role;
import com.semtleWebGroup.youtubeclone.domain.auth.token.AccessToken;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Map;

@Getter
public class TokenInfo {
    private final String email;
    private final Role role;
    private final Long userId;
    private final Long channelId;

    public TokenInfo(String email, Role role, Long userId, Long channelId) {
        this.email = email;
        this.role = role;
        this.userId = userId;
        this.channelId = channelId;
    }
    public static TokenInfo of (Map<AccessToken.Field,String> map){
        return new TokenInfo(
                map.get(AccessToken.Field.EMAIL),
                Role.valueOf(map.get(AccessToken.Field.ROLE)),
                Long.valueOf(map.get(AccessToken.Field.MEMBER_ID)),
                (StringUtils.hasText(map.get(AccessToken.Field.CHANNEL_ID))) ? Long.valueOf(map.get(AccessToken.Field.CHANNEL_ID)) : null
        );
    }
}
