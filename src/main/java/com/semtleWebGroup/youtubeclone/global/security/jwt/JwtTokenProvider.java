package com.semtleWebGroup.youtubeclone.global.security.jwt;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.member.domain.BlacklistedToken;
import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.repository.BlacklistedTokenRepository;
import com.semtleWebGroup.youtubeclone.domain.member.repository.MemberRepository;
import com.semtleWebGroup.youtubeclone.domain.member.service.MemberDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    private final MemberRepository memberRepository;
    @Value("${security.secretkey}")
    private String secretKey;
    public static final String TOKEN_PREFIX = "Bearer ";
    private static final long tokenValidTime = 30 * 60 * 1000L;     // 토큰 유효시간 30분
    private final MemberDetailsService memberDetailsService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    
    
    //secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    
    public String generateMemberToken(Member member) {
        Map<String, Object> header = new HashMap<>();
        header.put("prefix", TOKEN_PREFIX);
        
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("memberId", member.getId());
        payloads.put("CurrentChannelId", member.getCurrentChannelId());
        Set<Channel> channels = member.getChannels();
        for (Channel channel: channels) {
            payloads.put(channel.getTitle()+" channelId", channel.getId());
        }
        log.debug("SecurityContextHolder: {}", SecurityContextHolder.getContext().getAuthentication().toString());
        Date now = new Date();
        return Jwts.builder()
                .setHeader(header)
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    
    public Authentication getMemberAuthenticationByToken(String token) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(this.getMemberId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    
    public String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        log.debug("bearerToken: {}",bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * description : get user email by parsing token
     *
     * @param token
     * @return string
     * @author : yeachan
     */
    public String getMemberId(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        Integer memberId = (Integer) claims.get("memberId");
        Member member = memberRepository.findById(memberId).orElse(null);
        return Objects.requireNonNull(member).getEmail();
    }
    
    
    public boolean validateToken(String token) {
        if (blacklistedTokenRepository.existsByToken(token)) {
            log.debug("토큰이 blacklist에 존재하지 않습니다");
            return false; // Token is blacklisted
        }
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public void blacklistToken(String token) throws OptimisticLockingFailureException {
        BlacklistedToken blacklistedToken = BlacklistedToken.of(token);
        blacklistedTokenRepository.save(blacklistedToken);
    }
    
}