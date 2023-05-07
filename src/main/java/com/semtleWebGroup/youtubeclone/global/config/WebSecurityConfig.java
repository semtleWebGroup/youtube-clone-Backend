package com.semtleWebGroup.youtubeclone.global.config;

import com.semtleWebGroup.youtubeclone.domain.member.service.MemberDetailsService;
import com.semtleWebGroup.youtubeclone.global.error.exception.handler.MemberAccessDeniedHandler;
import com.semtleWebGroup.youtubeclone.global.error.exception.handler.MemberAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * package :  com.semtleWebGroup.youtubeclone.global.security.config
 * fileName : WebSecurityConfig
 * author :  ShinYeaChan
 * date : 2023-04-07
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    private final MemberDetailsService memberDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws
            Exception {
        http

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .cors()
                .and()
                .csrf()
                    .disable()

                .httpBasic()
                .disable()
                
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        
//                .and()
//                .logout().logoutSuccessUrl("/")
                
                .and()
                .authorizeRequests()
                
                .antMatchers("/members", "/members/session").permitAll()
                .anyRequest().authenticated()
                
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint());
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
        return http.build();
    }
    

}
