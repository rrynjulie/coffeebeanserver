package com.lec.spring.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

// 회원가입시 paramerter 받아오는 DTO
@Data
@NoArgsConstructor
public class UserJoinDTO {
    private String userName;
    private String password;
    private String nickName;
    private String email;
}
