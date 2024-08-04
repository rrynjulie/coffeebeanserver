package com.lec.spring.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // 로그인 인증을 시도할때 호출된다
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 인증시도하는  LoginFilter.attemptAuthentication() 호출");

        // request에서 userName. password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.printf("\t username:%s, password:%s\n", username, password);  // 검증 확인용.
        // 위 추출한 내용으로 인증 진행.
        // AuthenticationManager 에 username 과  password 를 넘겨주어 인증을 받아야 한다.
        // 이때! UsernamePasswordAuthenticationToken 에 담아서 넘겨주어야 한다!
        //  new UsernamePasswordAuthenticationToken(username, password, authorities);
        Authentication token = new UsernamePasswordAuthenticationToken(username.toUpperCase(), password, null);

        // 위 token 을 AuthenticationManager 에 전달하여, 로그인 검증을 받는다
        return authenticationManager.authenticate(token);
    }

    // 로그인(인증) 성공시 실행되는 메소드 (JWT 를 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("로그인 성공시 호출되는 LoginFilter.successfulAuthentication() 호출: 인증 성공!");
        System.out.println("\tAuthentication: " + authResult);

        super.successfulAuthentication(request, response, chain, authResult);
    }

    // 로그인 실패시 실행하는 메소드
    // 실패 원인은 AuthenticationException 를 보고 판단할수 있다
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("LoginFilter.unsuccessfulAuthentication() 호출: 인증 실패");
        //super.unsuccessfulAuthentication(request, response, failed);

        // 로그인 실패시 401 응답 코드 리턴
        response.setStatus(401);

    }
}
