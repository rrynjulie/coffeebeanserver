package com.lec.spring.jwt;

// 토근 payload에 저장될 정보
// userId
// userName
// password
// nickName
// email
// regDate
// reliability

// 자바에 있는 암호화 된 시크릿 키
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

// JWT 발급 및 검증
@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret){
        secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    //------------------------------------------------------------------
    // JWT 생성

    public String createJwt(
            Long id,
            String username,
            String nickname,
            String email,
            String regDate,
            Integer reliability,
            String role,
            Long expiredMs
    ){
        return Jwts.builder()
                .claim("id",id)
                .claim("username",username)
                .claim("nickname",nickname)
                .claim("email", email)
                .claim("regDate", regDate)
                .claim("reliability",reliability)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // 생성일
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료일시
                .signWith(secretKey)
                .compact();
    }

    // userId
    public Long getId(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);
    }

    // username
    public String getUsername(String token) {  // username 확인
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    // nickname 확인
    public String getNickname(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("nickname", String.class);
    }

    // email 확인
    public String getEmail(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    // regDate 확인
    public String getRegDate(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("regDate", String.class);
    }

    // reliability 확인
    public Integer getReliability(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("reliability", Integer.class);
    }

    // role
    public String getRole(String token) {  // role 확인
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // 만료일 확인
    public Boolean isExpired(String token) {  // 만료일 확인
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    // jwt 검증 메서드
    public boolean validateToken(String token){
        try{
            System.out.println("Validating JWT : " + token);
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.err.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }
}