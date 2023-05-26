package com.semtleWebGroup.youtubeclone.domain.auth.config;

import io.jsonwebtoken.security.WeakKeyException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties implements InitializingBean {

    private final String DEFAULT_SECRET = "asdlfkjldfksjflsdkajsldkfjasdfasfasdfasdfasdfasd";

    private Long tokenExpirationMsec = 864000000L; // 10 days
    private String secretKey = DEFAULT_SECRET;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (secretKey.equals(DEFAULT_SECRET)) {
            log.info("secret key is default value. this is not recommended on production server. please change secret key.");
        }
        if (secretKey.length() < 32) {
            // 어플리 케이션 실행을 종료시킴
            throw new IllegalArgumentException("secret key length is less than 256 bits (32 bytes) as mandated by the JWT JWA Specification (RFC 7518, Section 3.2)");
        }
        log.error("if the key byte array length is less than 256 bits (32 bytes) as mandated by the JWT JWA Specification (RFC 7518, Section 3.2) ");

    }
}
