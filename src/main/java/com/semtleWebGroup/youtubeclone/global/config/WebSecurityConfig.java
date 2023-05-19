package com.semtleWebGroup.youtubeclone.global.config;

import com.semtleWebGroup.youtubeclone.domain.member.api.MemberAccessDeniedHandler;
import com.semtleWebGroup.youtubeclone.domain.member.api.MemberAuthenticationEntryPoint;
import com.semtleWebGroup.youtubeclone.global.security.jwt.JwtAuthenticationFilter;
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

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
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
                
                .antMatchers("/members", "/members/session","/home").permitAll()
                .anyRequest().authenticated()
        
                .and()
                .logout()
                .logoutSuccessUrl("/home").permitAll()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint());
        return http.build();
    }
    

}
