package com.semtleWebGroup.youtubeclone.domain.member.repository;

import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}