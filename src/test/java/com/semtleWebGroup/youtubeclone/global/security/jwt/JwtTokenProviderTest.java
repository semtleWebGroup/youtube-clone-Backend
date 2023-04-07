package com.semtleWebGroup.youtubeclone.global.security.jwt;

import com.semtleWebGroup.youtubeclone.global.config.WebMvcConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * package :  com.semtleWebGroup.youtubeclone.global.security.jwt
 * fileName : JwtTokenProviderTest
 * author :  ShinYeaChan
 * date : 2023-04-08
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class JwtTokenProviderTest {
    
    @MockBean
    private WebMvcConfig webMvcConfig;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @Mock
    private UserDetailsService userDetailsService;
    
    @Mock
    private HttpServletRequest httpServletRequest;
    
    private final String email = "test@test.com";
    private final List<String> roles = Collections.singletonList("ROLE_USER");
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    }
    
    @Test
    void createToken() {
        String token = jwtTokenProvider.createToken(email, roles);
        
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }
    
    @Test
    void getAuthentication() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        String token = jwtTokenProvider.createToken(email, roles);
        
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        
        assertNotNull(authentication);
        assertEquals(authentication.getPrincipal(), userDetails);
    }
    
    @Test
    void getUserEmail() {
        String token = jwtTokenProvider.createToken(email, roles);
        
        String userEmail = jwtTokenProvider.getUserEmail(token);
        
        assertEquals(userEmail, email);
    }
    
    @Test
    void validateToken() {
        Date now = new Date();
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 1000))
                .signWith(SignatureAlgorithm.HS256, "secretKey")
                .compact();
        
        boolean result = jwtTokenProvider.validateToken(token);
        
        assertTrue(result);
    }
    
    @Test
    void resolveToken() {
        String token = "Bearer " + jwtTokenProvider.createToken(email, roles);
        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        
        String result = jwtTokenProvider.resolveToken(httpServletRequest);
        
        assertEquals(result, jwtTokenProvider.createToken(email, roles));
    }
}