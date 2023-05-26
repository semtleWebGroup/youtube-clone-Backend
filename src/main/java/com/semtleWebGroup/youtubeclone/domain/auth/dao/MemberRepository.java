package com.semtleWebGroup.youtubeclone.domain.auth.dao;

import com.semtleWebGroup.youtubeclone.domain.auth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Member findByEmail(String email);
}
