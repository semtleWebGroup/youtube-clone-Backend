package com.semtleWebGroup.youtubeclone.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "blacklisted_tokens")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;
    
    public BlacklistedToken(String token) {
        this.token = token;
    }
    
    public static BlacklistedToken of(String token) {
        return new BlacklistedToken(token);
    }
}
