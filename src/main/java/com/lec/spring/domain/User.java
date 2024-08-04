package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

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
    private String password;

    // 닉네임
    @Column(nullable = false)
    private String nickName;

    // 전화번호
//     private String phonenum;

    //이메일
    @Column(nullable = false)
    private String email;

    // 가입 날짜
    private LocalDateTime regDate;

    // 신뢰도
    @Column(nullable = false)
    @ColumnDefault("500")
    private int reliability;

    // 회원사진
    // TODO

    // -----------------------------------------------------------
    // 권한
    private String role; //  "ROLE_USER", "ROLE_USER,ROLE_ADMIN"

}
