package com.lec.spring.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTUtilTest {
    @Autowired
    private JWTUtil jwtUtil;

    @Test
    void test() {

        String jwtToken = jwtUtil.createJwt(
                4L,
                "user1",
                "1",
                "1.@mail.com",
                "2024-08-02 17:35:28.031913",
                500,
                "ROLE_USER",
                0,
                5000L
        );
        System.out.println(jwtToken);

        System.out.println("""
           id: %s
           username: %s
           nickname: %s
           email: %s
           regDate: %s
           reliability: %d
           role: %s
           memberStatus: %s
           isExpired: %s
       """.formatted(
                jwtUtil.getId(jwtToken),
                jwtUtil.getUsername(jwtToken),
                jwtUtil.getNickname(jwtToken),
                jwtUtil.getEmail(jwtToken),
                jwtUtil.getRegDate(jwtToken),
                jwtUtil.getReliability(jwtToken),
                jwtUtil.getRole(jwtToken),
                jwtUtil.getMemberStatus(jwtToken),
                jwtUtil.isExpired(jwtToken)));
    }
}