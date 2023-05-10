package com.semtleWebGroup.youtubeclone.domain.member.service;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MemberDetailsService implements UserDetailsService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByName(username);
        if(member.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new User(member.get().getName(),member.get().getPassword(),new ArrayList<>());
    }
}
