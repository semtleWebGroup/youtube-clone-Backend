package com.semtleWebGroup.youtubeclone.domain.member.api;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignInRequestDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignInResponseDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignUpRequestDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignUpResponseDto;
import com.semtleWebGroup.youtubeclone.domain.member.service.MemberSignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequestMapping("/members")
@Slf4j
@RequiredArgsConstructor
public class MemberApi {
    private final MemberSignService memberSignService;
    
    
    @PostMapping("")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequestDto signInRequestDto) throws IOException {
        log.debug("로그인");
        SignInResponseDto dto = memberSignService.signIn(signInRequestDto);
        if (dto.getSuccess())
            return ResponseEntity.status(HttpStatus.OK).header("Authentication", dto.getMemberToken()).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            memberSignService.signOut(request);
            return ResponseEntity.status(HttpStatus.OK).build();
            
        } catch (IllegalArgumentException e) {
            log.error("An error occurred during logout", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Transactional
    @PostMapping("/session")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDto memberDto) {
        try {
            log.info("Register: {}", memberDto.toString());
            Member member = Member.builder()
                    .email(memberDto.getEmail())
                    .name(memberDto.getName())
                    .role(memberDto.getRole())
                    .password(memberDto.getPassword())
                    .build();
            SignUpResponseDto dto = memberSignService.signUp(member);
            if (dto.getSuccess())
                return ResponseEntity.status(HttpStatus.CREATED).body(dto);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
        } catch (Exception e) {
            log.error("An error occurred while signing up", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
}