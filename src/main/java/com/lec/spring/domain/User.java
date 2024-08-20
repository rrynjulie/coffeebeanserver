package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user")
public class User {

    // 회원 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 이름
    @Column(nullable = false)
    private String userName;

    // 비밀번호
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    // 닉네임
    @Column(nullable = false)
    private String nickName;

    // 전화번호
//     private String phonenum;

    //이메일
    @Column(nullable = true)
    private String email;

    // 가입 날짜
    private LocalDateTime regDate;

    // 신뢰도
    @Column(nullable = false)
    @ColumnDefault("500")
    private int reliability;

    // OAuth2 Client
    @Column
    private String provider;
    private String providerId;

    // 회원사진
    // TODO

    // -----------------------------------------------------------
    // 권한
    private String role; //  "ROLE_USER", "ROLE_USER,ROLE_ADMIN"

    // 날짜 자동 저장
    // reliability 자동으로 500 설정
    @PrePersist
    protected void onCreate() {
        this.regDate = LocalDateTime.now();
        if (this.reliability == 0) { // reliability 값이 설정되지 않은 경우 기본값 500을 설정
            this.reliability = 500;
        }
    }
}
