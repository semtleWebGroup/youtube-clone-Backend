package com.semtleWebGroup.youtubeclone.domain.auth.config;

import com.semtleWebGroup.youtubeclone.domain.auth.dao.MemberRepository;
import com.semtleWebGroup.youtubeclone.domain.auth.domain.Member;
import com.semtleWebGroup.youtubeclone.domain.auth.domain.Role;
import com.semtleWebGroup.youtubeclone.domain.auth.exception.GlobalAuthFailEntryPoint;
import com.semtleWebGroup.youtubeclone.domain.auth.filter.JwtAuthorizationFilter;
import com.semtleWebGroup.youtubeclone.domain.auth.token.AccessToken;
import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Base64;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessToken.TokenBuilder accessTokenBuilder(AuthProperties authProperties) {
        return AccessToken.builder(Base64.getEncoder().encodeToString(authProperties.getSecretKey().getBytes()), authProperties.getTokenExpirationMsec(), SignatureAlgorithm.HS256);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security, AccessToken.TokenBuilder tokenBuilder) throws Exception {

        // disables
        return security
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .cors().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(req -> req
                        .antMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling()
                .authenticationEntryPoint(new GlobalAuthFailEntryPoint())
                .and()
                //jwt authorization
                .addFilterBefore(new JwtAuthorizationFilter(tokenBuilder), BasicAuthenticationFilter.class)
                .build();

    }


    @Bean
    public CommandLineRunner testMemberInitializer(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Channel channel1 = Channel.builder()
                    .title("channel1")
                    .description("channel1 des")
                    .build();
            Channel channel2 = Channel.builder()
                    .title("channel2")
                    .description("channel2 des")
                    .build();

//            Member member = new Member("test@a.a", passwordEncoder.encode("1234"), Role.ROLE_USER, List.of(channel1, channel2));
//            memberRepository.save(member);
        };
    }
}