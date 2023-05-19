package com.semtleWebGroup.youtubeclone.domain.member.service;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignCommonResponse;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignInRequestDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignInResponseDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignUpResponseDto;
import com.semtleWebGroup.youtubeclone.domain.member.repository.MemberRepository;
import com.semtleWebGroup.youtubeclone.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
@RequiredArgsConstructor
public class MemberSignService{
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    
    public SignUpResponseDto signUp(Member member) {
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        if (!memberRepository.existsByEmail(member.getEmail())) {
            memberRepository.save(member);
            log.info("sign up dto: {}", signUpResponseDto);
            //TODO: 회원가입 후 토큰 넘기기 고려
            setSuccessResult(signUpResponseDto);
            log.debug("SecurityContextHolder: {}", SecurityContextHolder.getContext().getAuthentication().toString());
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
            member.setTimeAndDefaultChannel();
            if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
                setFailResult(signInResultDto);
                return signInResultDto;
            }
            String memberToken = jwtTokenProvider.generateMemberToken(member);
            
            signInResultDto = SignInResponseDto.builder()
                    .memberToken(memberToken)
                    .build();
            setSuccessResult(signInResultDto);
            return signInResultDto;
        }
        setFailResult(signInResultDto);
        return signInResultDto;
    }
    
    public void signOut(HttpServletRequest request) {
        try{
            jwtTokenProvider.blacklistToken(jwtTokenProvider.parseBearerToken(request));
            SecurityContextHolder.clearContext();
        }
        catch(OptimisticLockingFailureException e){
            throw new IllegalArgumentException("save failed");
        }
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
