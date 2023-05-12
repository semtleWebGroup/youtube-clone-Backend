package com.semtleWebGroup.youtubeclone.global.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semtleWebGroup.youtubeclone.domain.channel.application.ChannelAuthentication;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.channel.repository.ChannelRepository;
import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.service.MemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    
    private String secretKey = "secretKey";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    private static final long tokenValidTime = 30 * 60 * 1000L;     // 토큰 유효시간 30분
    private final MemberDetailsService memberDetailsService;
    
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private ChannelRepository channelRepository;
    
    
    //secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    
    public String generateMemberToken(Member member) {
        Map<String, Object> header = new HashMap<>();
        header.put("prefix", TOKEN_PREFIX);
        
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("member", member);
        
        Date now = new Date();
        return Jwts.builder()
                .setHeader(header)
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    public String generateChannelToken(String memberToken,Channel channel) throws IOException {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(memberToken).getBody();
        Object memberObj = claims.get("member");
        String memberJson = mapper.writeValueAsString(memberObj);
        Member member = mapper.readValue(memberJson, Member.class);
        
        Date now = new Date();
        Map<String, Object> header = new HashMap<>();
        header.put("prefix", TOKEN_PREFIX);
        
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("memberId",member.getId());
        payloads.put("channel", channel);
        
        return  Jwts.builder()
                .setHeader(header)
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    public Authentication getMemberAuthentication(String token) throws JsonProcessingException {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    public Authentication getChannelAuthentication(String channelToken) throws JsonProcessingException {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(channelToken).getBody();
        Object channelObj = claims.get("channel");
        String channelJson = mapper.writeValueAsString(channelObj);
        Channel channel = mapper.readValue(channelJson, Channel.class);
        return new ChannelAuthentication(channel);
    }
    
    /**
     * description : get user email by parsing token
     *
     * @param token
     * @return string
     * @author : yeachan
     */
    public String getUserEmail(String token) throws JsonProcessingException {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        Object memberObj = claims.get("member");
        String memberJson = mapper.writeValueAsString(memberObj);
        Member member = mapper.readValue(memberJson, Member.class);
        log.info("토큰에서 가져온 이메일 :{}", member.getEmail());
        return member.getEmail();
    }

//    public Channel getChannel(String token){
//        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//        return (Channel) claims.get("channel");
//    }
    
    // 토큰 유효성, 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isChannelToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.containsKey("channel");
    }
}