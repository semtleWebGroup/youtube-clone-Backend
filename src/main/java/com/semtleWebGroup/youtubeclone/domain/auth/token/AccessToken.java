package com.semtleWebGroup.youtubeclone.domain.auth.token;

import com.semtleWebGroup.youtubeclone.domain.auth.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.auth.util.AES256;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

public class AccessToken extends Token {
    public enum Field {
        EMAIL,
        ROLE,
        MEMBER_ID,
        CHANNEL_ID
    }


    private AccessToken(String value, String tokenSecretKey, SignatureAlgorithm signatureAlgorithm) {
        super(value, tokenSecretKey, signatureAlgorithm);
    }

    public static TokenBuilder builder(String tokenSecretKey, Long accessTokenExpirationMsec, SignatureAlgorithm signatureAlgorithm){
        return new TokenBuilder(tokenSecretKey, accessTokenExpirationMsec, signatureAlgorithm);
    }

    /**
     * 토큰을 쉽게 생성하기 위한 Builder Pattern
     */
    public static class TokenBuilder {
        private final String tokenSecretKey;
        private final Long accessTokenExpirationMsec;
        private final SignatureAlgorithm signatureAlgorithm;

        public TokenBuilder(String tokenSecretKey, Long accessTokenExpirationMsec, SignatureAlgorithm signatureAlgorithm) {
            this.tokenSecretKey = tokenSecretKey;
            this.accessTokenExpirationMsec = accessTokenExpirationMsec;
            this.signatureAlgorithm = signatureAlgorithm;
        }

        public AccessToken build(Member member, Long channelId){
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + accessTokenExpirationMsec);

            String encryptMemberId = AES256.encrypt(member.getId().toString());
            String encryptChannelId = "";
            if (channelId != null) {
                encryptChannelId = AES256.encrypt(channelId.toString());
            }


            String value = Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setSubject(TokenType.ACCESS_TOKEN.name())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .claim(Field.EMAIL.name(), member.getEmail().toString())
                    .claim(Field.ROLE.name(), member.getRole().name())
                    .claim(Field.MEMBER_ID.name(), encryptMemberId)
                    .claim(Field.CHANNEL_ID.name(), encryptChannelId)
                    .signWith(signatureAlgorithm, tokenSecretKey)
                    .compact();
            return new AccessToken(value, tokenSecretKey,signatureAlgorithm);
        }


        public AccessToken build(Member member){
            return this.build(member, null);
        }

        public AccessToken build(String token){
            return new AccessToken(token, tokenSecretKey,signatureAlgorithm);
        }
    }

    @Override
    public Map<Field, String> parseClaims() {
        Map<Field, String> fieldStringMap = super.parseClaims();
        //decrypt
        String encryptMemberId = fieldStringMap.get(Field.MEMBER_ID);
        String encryptChannelId = fieldStringMap.get(Field.CHANNEL_ID);

        String memberId = AES256.decrypt(encryptMemberId);
        String channelId = "";
        if (StringUtils.hasText(encryptChannelId)) channelId = AES256.decrypt(encryptChannelId);

        //replace entry
        fieldStringMap.replace(Field.MEMBER_ID, memberId);
        fieldStringMap.replace(Field.CHANNEL_ID, channelId);

        return fieldStringMap;

    }
}