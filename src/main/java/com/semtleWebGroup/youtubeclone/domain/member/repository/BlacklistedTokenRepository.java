package com.semtleWebGroup.youtubeclone.domain.member.repository;

import com.semtleWebGroup.youtubeclone.domain.member.domain.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);
}
