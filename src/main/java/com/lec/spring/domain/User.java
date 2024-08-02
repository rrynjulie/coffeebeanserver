package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User {

    // 회원 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    // 이름
    @Column(nullable = false)
    private String username;

    // 닉네임
    @Column(nullable = false)
    private String nickname;

    // 비밀번호
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    // 전화번호
    // private String phonenum;

    // 생년월일
    private String birthday;

    //이메일
    @Column(nullable = false)
    private String email;

    // 가입 날짜
    private LocalDateTime reg_date;

    // 신뢰도
    @Column(nullable = false)
    @ColumnDefault("500")
    private int reliability;

    // 회원사진
    // TODO

    // ---------------------------------------
    // 권한
    private String role; //  "ROLE_USER", "ROLE_USER, ROLE_ADMIN"

}
