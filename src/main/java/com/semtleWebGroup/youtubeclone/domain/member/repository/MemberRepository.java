package com.semtleWebGroup.youtubeclone.domain.member.repository;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByChannels_Id(Long id);
    boolean existsByEmail(String email);
    Optional<Member> findByName(String name);
    
    Optional<Member> findByEmail(String email);
}