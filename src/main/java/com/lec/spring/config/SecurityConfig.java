package com.lec.spring.config;

// 스프링 시큐리티를 사용하기 위해서는
// 스프링 시큐리티에 필요한 bean을 추가해 주는
// config 클래스 파일을 추가해 줘야 합니다.

import com.lec.spring.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // bean으로 생성해 준다
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 패스워드 암호화를 위한 BCryptPasswordEncoder 를 빈에 추가
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 다 필요없는 auth는 비활시켜준다.
        // csrf disable
        http.csrf((auth) -> auth.disable());

        // Form 로그인 disable
        http.formLogin((auth) -> auth.disable());

        // http baisc 인증방식 disable
        http.httpBasic((auth) -> auth.disable());


        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin").hasRole("ADMIN") //  /admin 경로는 ADMIN 권한이 있을 때만 가능하다
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN") // /member 경로는 멤버나 어드민 둘중에 하나만 있어도 가능하다
                        .anyRequest().permitAll() // 나머지는 다 프리패스
                );

        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 사용 안함.

        // 필터 추가 LoginFilter()는 AuthenticationManager 인자를 받음
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
