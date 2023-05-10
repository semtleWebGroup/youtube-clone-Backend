package com.semtleWebGroup.youtubeclone.domain.member.api;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignUpRequestDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignInResponseDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignUpResponseDto;
import com.semtleWebGroup.youtubeclone.domain.member.dto.SignInRequestDto;
import com.semtleWebGroup.youtubeclone.domain.member.service.MemberSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping( "/members")
@Slf4j
public class MemberApi {
    @Autowired
    private MemberSignService memberSignService;
    
    @PostMapping("")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto signInRequestDto){
        log.info("로그인");
        SignInResponseDto dto = memberSignService.signIn(signInRequestDto);
        if(dto.getSuccess())
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }
    
    @PostMapping("/session")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto memberDto) {
        try {
            log.info("Register");
            System.out.println(memberDto.toString());
            Member member=Member.builder()
                    .email(memberDto.getEmail())
                    .name(memberDto.getName())
                    .role(memberDto.getRole())
                    .build();
            SignUpResponseDto dto = memberSignService.signUp(member);
            if(dto.getSuccess())
                return  ResponseEntity.status(HttpStatus.OK).body(dto);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
        } catch (Exception e) {
            log.error("An error occurred while signing up", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}