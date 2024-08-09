package com.lec.spring.config;

// 스프링 시큐리티를 사용하기 위해서는
// 스프링 시큐리티에 필요한 bean을 추가해 주는
// config 클래스 파일을 추가해 줘야 합니다.

import com.lec.spring.jwt.JWTFilter;
import com.lec.spring.jwt.JWTUtil;
import com.lec.spring.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration // bean으로 생성해 준다
//@EnableWebSecurity(debug = true)
@EnableWebSecurity
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private List<String> corsAllowedOrigins;

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
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

        // JWTFilter 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        // 필터 추가 LoginFilter()는 AuthenticationManager 인자를 받음
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);


        // cors 설정
        http
                .cors(corsConfigurer
                                -> corsConfigurer.configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();
                                // setAllowedOrigins(List<String>) Cors요청을 허용할 도메인(들) 설정.
                                // setAllowedMethods(List<String>) Cors요청을 허용할 request method(들) 설정
                                // setAllowCredentials(Boolean)  user credential 지원 여부.  (쿠키등..)
                                // setAllowedHeaders(List<String>) Cors요청을 허용할 헤더(들) 설정
                                // setMaxAge(Long)  preflight 요청에 대한 응답을 브라우저에서 캐싱하는 시간
                                // setExposedHeaders(List<String>)  응답헤더 설정(들) 추가
                                configuration.setAllowedOrigins(corsAllowedOrigins);
                                configuration.setAllowedMethods(List.of("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));  // List.of("*") 와 동일
                                configuration.setMaxAge(3600L);

                                configuration.setExposedHeaders(List.of("Authorization"));

                                return configuration;
                            }
                        }));
        return http.build();
    }
}