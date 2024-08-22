package com.lec.spring.jwt;

import com.lec.spring.config.PrincipalDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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
        Authentication authentication = authenticationManager.authenticate(token);

        // memberStatus 확인
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        if (userDetails.getUser().getMemberStatus() == 1) {
            throw new AuthenticationException("회원 탈퇴된 계정입니다.") {
            };
        }

        return authentication;
    }

    // 로그인(인증) 성공시 실행되는 메소드 (JWT 를 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("로그인 성공시 호출되는 LoginFilter.successfulAuthentication() 호출: 인증 성공!");
        System.out.println("\tAuthentication: " + authResult);

        PrincipalDetails userDetails = (PrincipalDetails) authResult.getPrincipal();
        // jwt에 담을 내용

        // 유저 아이디
        Long userId = userDetails.getUser().getUserId();

        // 유저 이름
        String userName = userDetails.getUsername();

        // 유저 닉네임
        String nickName = userDetails.getNickname();

        // 유저 이메일
        String email = userDetails.getEmail();

        // 유저 등록일
        LocalDateTime regDate = userDetails.getRegDate();
        String regDate2 = regDate.toString ();

        // 유저 신뢰도
        Integer reliability = userDetails.getReliability();

        // 유저 탈퇴여부
        Integer memberStatus = userDetails.getMemberStatus();

        // 권한
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String role = authorities.stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(","));

        String token = jwtUtil.createJwt(
                userId,
                userName,
                nickName,
                email,
                regDate2,
                reliability,
                role,
                memberStatus,
                30 * 60 * 1000L );

        response.addHeader("Authorization", "Bearer " + token);
    }

    // 로그인 실패시 실행하는 메소드
    // 실패 원인은 AuthenticationException 를 보고 판단할수 있다
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("LoginFilter.unsuccessfulAuthentication() 호출: 인증 실패");
        //super.unsuccessfulAuthentication(request, response, failed);

        // 기본 메시지 설정
        String errorMessage = "Invalid credentials";

        // 특정 예외 메시지에 따라 오류 메시지를 설정
        if ("회원 탈퇴된 계정입니다.".equals(failed.getMessage())) {
            errorMessage = "Account is deactivated";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(errorMessage);

    }
}