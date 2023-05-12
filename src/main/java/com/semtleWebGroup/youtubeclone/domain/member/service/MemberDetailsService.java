package com.semtleWebGroup.youtubeclone.domain.member.service;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class MemberDetailsService implements UserDetailsService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isEmpty()) {
            //TODO: 예외처리 조정
            throw new UsernameNotFoundException(email);
        }
        return member.get();
    }
}
