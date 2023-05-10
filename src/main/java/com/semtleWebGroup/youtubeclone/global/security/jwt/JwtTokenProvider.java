package com.semtleWebGroup.youtubeclone.global.security.jwt;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.domain.Role;
import com.semtleWebGroup.youtubeclone.domain.member.service.MemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    //secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    
    
    public String generateMemberToken(Member member) {
        Map<String, Object> header =  new HashMap<>();
        header.put("prefix", TOKEN_PREFIX);
        
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("member",member);
        
        Date now = new Date();
        return Jwts.builder()
                .setHeader(header)
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    public Set<String> generateChannelTokens(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        Member member = (Member) claims.get("member");
        Set<Channel> channels = member.getChannels();

        Set<String> tokens=new LinkedHashSet<>();
        for (Channel channel : channels) {
            Date now = new Date();
            Map<String, Object> header =  new HashMap<>();
            header.put("prefix", TOKEN_PREFIX);
            
            Map<String, Object> payloads = new HashMap<>();
            payloads.put("title",channel.getTitle());
            payloads.put("channelId",channel.getId());
            
            tokens.add(Jwts.builder()
                    .setHeader(header)
                    .setClaims(payloads)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + tokenValidTime))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact());
        }
        return tokens;
    }
    
    public Authentication getMemberAuthentication(String token) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    public Authentication getChannelAuthentication(String channelToken) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(channelToken).getBody();
        Channel channel = (Channel) claims.get("channel");
        //principal을 id로 넣어도 되나..?
        return new PreAuthenticatedAuthenticationToken(channel.getId(), channel, AuthorityUtils.createAuthorityList(Role.User.getRoleName()));
    }
    
    /**
     * description : get user email by parsing token
     *
     * @param token
     * @return string
     * @author : yeachan
     */
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
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