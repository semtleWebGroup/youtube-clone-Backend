
package com.semtleWebGroup.youtubeclone.domain.member.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "member")
@AllArgsConstructor
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Integer id;
    
    @Size(max = 45)
    @NotNull
    @Column(name = "email", nullable = false, length = 45)
    private String email;
    
    @Size(max = 15)
    @NotNull
    @Column(name = "name", nullable = false, length = 15)
    private String name;
    
    @Size(max = 60)
    @NotNull
    @Column(name = "password", nullable = false, length = 60)
    private String password;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Channel> channels = new LinkedHashSet<>();
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }
    
    @Override
    public String getUsername() {
        return this.email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}