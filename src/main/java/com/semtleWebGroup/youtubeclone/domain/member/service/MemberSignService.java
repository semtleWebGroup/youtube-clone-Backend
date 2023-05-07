package com.semtleWebGroup.youtubeclone.domain.member.service;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignCommonResponse;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignInRequestDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignInResponseDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignUpResponseDto;
import com.semtleWebGroup.youtubeclone.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * package :  com.semtleWebGroup.youtubeclone.domain.member.service
 * fileName : MemberSignService
 * author :  ShinYeaChan
 * date : 2023-05-05
 */
@Service
@Slf4j
@Transactional
public class MemberSignService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public SignUpResponseDto signUp(Member member) {
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        if (!memberRepository.existsByEmail(member.getEmail())) {
            memberRepository.save(member);
            log.info("sign up dto: {}", signUpResponseDto);
            //TODO: 회원가입 후 토큰 넘기기 고려
            setSuccessResult(signUpResponseDto);
            return signUpResponseDto;
        }
        setFailResult(signUpResponseDto);
        return signUpResponseDto;
    }
    
    public SignInResponseDto signIn(SignInRequestDto dto) throws RuntimeException {
        Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
        SignInResponseDto signInResultDto= SignInResponseDto.builder().build();
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
                setFailResult(signInResultDto);
                return signInResultDto;
            }
            signInResultDto = SignInResponseDto.builder()
                    .token(jwtTokenProvider.generateToken(String.valueOf(member.getEmail()),
                            member.getRole().getRoleName()))
                    .build();
            setSuccessResult(signInResultDto);
            return signInResultDto;
        }
        setFailResult(signInResultDto);
        return signInResultDto;
    }
    
    private void setSuccessResult(SignUpResponseDto result) {
        log.debug("success response");
        result.setSuccess(true);
        result.setCode(SignCommonResponse.SUCCESS.getCode());
        result.setMsg(SignCommonResponse.SUCCESS.getMsg());
    }
    
    private void setFailResult(SignUpResponseDto result) {
        log.debug("failure response");
        result.setSuccess(false);
        result.setCode(SignCommonResponse.FAIL.getCode());
        result.setMsg(SignCommonResponse.FAIL.getMsg());
    }
}
