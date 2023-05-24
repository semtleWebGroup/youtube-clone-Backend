package com.semtleWebGroup.youtubeclone.domain.auth.api;


import com.semtleWebGroup.youtubeclone.domain.auth.dao.MemberRepository;
import com.semtleWebGroup.youtubeclone.domain.auth.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.auth.domain.Role;
import com.semtleWebGroup.youtubeclone.domain.auth.dto.ChannelCandidateResponse;
import com.semtleWebGroup.youtubeclone.domain.auth.dto.FormBody;
import com.semtleWebGroup.youtubeclone.domain.auth.exception.common.OccupiedEmailException;
import com.semtleWebGroup.youtubeclone.domain.auth.token.AccessToken;
import com.semtleWebGroup.youtubeclone.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthApi {

    private final AccessToken.TokenBuilder tokenBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/auth/login")
    public ResponseEntity<ChannelCandidateResponse> login(@Valid @RequestBody FormBody formBody) {
        //1. db 조회
        Member member = memberRepository.findByEmail(formBody.getEmail());

        if (member == null) {
            log.debug("try login with unknown email {} ", formBody.getEmail());
            throw new BadCredentialsException("email not found");
        }

        //2. 비밀번호 확인
        boolean matches = passwordEncoder.matches(formBody.getPassword(), member.getPassword());
        if (!matches) {
            log.debug("try login with wrong password");
            throw new BadCredentialsException("password not found");
        }

        //3. 토큰 생성
        AccessToken accessToken = tokenBuilder.build(member);
        log.debug("login success and token published");

        //4. 채널 리스트 바디 생성
        List<ChannelCandidateResponse.Entry> entries = member.getChannels().stream()
                .map(channel -> new ChannelCandidateResponse.Entry(channel.getTitle(), channel.getId()))
                .collect(Collectors.toList());
        ChannelCandidateResponse channelCandidateResponse = new ChannelCandidateResponse(entries);

        //4. 토큰 응답
        return ResponseEntity.ok()
                .header("Authorization", "Bearer  " + accessToken.getValue())
                .body(channelCandidateResponse);
    }


    @PostMapping("/auth/channel")
    public ResponseEntity<Void> channel(@RequestBody Map<String, String> requestBody, @RequestHeader("Authorization") String authorization) {
        //1. valid request
        boolean isHeaderValid = StringUtils.hasText(authorization) && authorization.startsWith("Bearer ");
        boolean isBodyValid = requestBody.containsKey("key");
        if (!isHeaderValid || !isBodyValid) {
            throw new BadRequestException(Collections.emptyList());
        }

        //2. 토큰 파싱
        String tokenValue = authorization.substring(7);
        AccessToken token = tokenBuilder.build(tokenValue);
        Map<AccessToken.Field, String> fieldStringMap = token.parseClaims();
        Long memberId = Long.valueOf(fieldStringMap.get(AccessToken.Field.MEMBER_ID));

        //3. body 파싱
        Long wantedChannelId = Long.valueOf(requestBody.get("key"));

        //4. 원하는 채널 아이디가 유효한지 확인
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BadCredentialsException("token claim - memberId is not valid"));
        boolean isValidChannelId = member.getChannels().stream().anyMatch(channel -> channel.getId().equals(wantedChannelId));

        if (!isValidChannelId){
            throw new BadRequestException(Collections.emptyList());
        }

        //5. 토큰에 channelId 넣어서 재생성
        AccessToken newToken = tokenBuilder.build(member, wantedChannelId);

        //6. 토큰 응답
        log.debug("token event - channel changed");
        return ResponseEntity.ok()
                .header("Authorization", "Bearer  " + newToken.getValue())
                .build();
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() {
        //그냥 간단하게, 토큰 없에서 리턴
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Void> register(@Valid @RequestBody FormBody formBody) {
        //1. db 저장
        Member member = new Member(formBody.getEmail(), passwordEncoder.encode(formBody.getPassword()), Role.ROLE_USER, List.of());
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e){
            if (e.getMessage().toUpperCase().contains("member_email_unique".toUpperCase())){
                throw new OccupiedEmailException("occupied email "+ formBody.getEmail()); //handled by Global Exception Handler
            } else {
                throw e;
            }
        }
        log.debug("register success");
        return ResponseEntity.ok().build();
    }
}
