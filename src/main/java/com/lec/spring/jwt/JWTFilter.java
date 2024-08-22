package com.lec.spring.jwt;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

public class JWTFilter extends OncePerRequestFilter {
    // 요청 URL 및 메소드 출력
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("doFilterInternal() 호출 - 요청 URL: " + request.getRequestURI() + " , 메소드: " + request.getMethod());

        // request 에서 Authorization 헤더를 찾음 (JWT 있는지?)
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if(authorization == null || !authorization.startsWith("Bearer ")){
            // JWT 가 없다!
            System.out.println("\t JWT 없음");
            filterChain.doFilter(request, response);  // 리턴하기 전에 다음 필터에 넘기고 종료!
            return;
        }

        // JWT 토큰 획특
        String token = authorization.split(" ")[1];
        System.out.println("\t 토큰 획득 authorization now, token: " + token);

        // 토큰 소멸 시간 검증
        if(jwtUtil.isExpired(token)){
            System.out.println("토큰 소멸시간 초과 ");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 정보 획득
        Long id = jwtUtil.getId(token);
        String username = jwtUtil.getUsername(token);
        String nickname = jwtUtil.getNickname(token);
        String email = jwtUtil.getEmail(token);
        String regDate = jwtUtil.getRegDate(token);
        Integer reliability = jwtUtil.getReliability(token);
        Integer memberStatus = jwtUtil.getMemberStatus(token);
        String role = jwtUtil.getRole(token);

        // User 생성하여 로그인 진행
        User user = User.builder()
                .userId(id)
                .userName(username)
                .password("temppassword") // 임시 비밀번호
                .nickName(nickname)
                .email(email)
                .regDate(LocalDateTime.parse(regDate))
                .reliability(reliability)
                .memberStatus(memberStatus)
                .role(role)
                .build();

        // UserDetails 에 User 담아 생성
        PrincipalDetails userDetails = new PrincipalDetails(user);

        // 스프링 시큐리티 인증 토큰 생성 => Authentication
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 세션에 위 Authentication을 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음필터로 전달
        filterChain.doFilter(request, response);
    }
}