package com.lec.spring.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailDto {

    // 이메일 주소
    private String email;
    // 인증코드
    private String verifyCode;
}
