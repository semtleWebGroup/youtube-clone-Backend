package com.semtleWebGroup.youtubeclone.domain.auth.token;

import io.jsonwebtoken.*;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@EqualsAndHashCode(of = "value")
public abstract class Token {

    public enum TokenStatus {
        VALID,
        INVALID,
        EXPIRED
    }

    public enum TokenType {
        ACCESS_TOKEN,
        REFRESH_TOKEN
    }

    protected String value;

    private final JwtParser jwtParser;
    private final SignatureAlgorithm signatureAlgorithm;

    public Token(String value, String tokenSecretKey, SignatureAlgorithm signatureAlgorithm) {
        this.value = value;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(tokenSecretKey).build();
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Map<AccessToken.Field,String> parseClaims(){
        Claims body = jwtParser
                .parseClaimsJws(this.value).getBody();

        Map<AccessToken.Field,String> claims = new HashMap<>();
        for (AccessToken.Field field : AccessToken.Field.values()){
            claims.put(field, body.get(field.name(), String.class));
        }
        return claims;
    }


    public Token.TokenStatus validateToken() {
        try {
            this.parseClaims();
            return Token.TokenStatus.VALID;
        } catch (SignatureException e){
            log.debug("{} >> invalid signature : claimsJwt string is actually a JWS and signature validation fails",this.getClass().getName());
            return Token.TokenStatus.INVALID;
        } catch (MalformedJwtException e){
            log.debug("{} >> invalid claimsJwt : the Jwt is not a valid",this.getClass().getName());
            return Token.TokenStatus.INVALID;
        } catch (ExpiredJwtException e) {
            log.debug("{} >> expired claimsJwt : the Jwt is expired", this.getClass().getName());
            return Token.TokenStatus.EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.debug("{} >> unsupported claimsJwt : the claimsJwt argument does not represent an unsigned Claims JWT",this.getClass().getName());
            return Token.TokenStatus.INVALID;
        }
    }



    public String getValue() {
        return value;
    }

    protected void setValue(String value) {
        this.value = value;
    }

}
