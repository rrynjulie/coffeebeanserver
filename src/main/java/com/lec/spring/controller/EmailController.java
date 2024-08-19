package com.lec.spring.controller;

import com.lec.spring.domain.Email;
import com.lec.spring.domain.EmailDto;
import com.lec.spring.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService){
        this.emailService = emailService;
    }

    // 이메일 주소로 인증 코드를 발송
    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody EmailDto emailDto) {
        String code = emailService.sendVerificationCode(emailDto.getEmail());
        return ResponseEntity.ok("발송된 확인코드: " + emailDto.getEmail());
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody EmailDto emailDto) {
        boolean isVerified = emailService.verifyEmailCode(emailDto.getEmail(), emailDto.getVerifyCode());

        Map<String, Object> response = new HashMap<>();
        if (isVerified) {
            response.put("success", true);
            response.put("message", "메일이 성공적으로 확인 되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "유효하지 않거나 만료된 검증코드");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean isTaken = emailService.isEmailRegistered(email);
        if (isTaken) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Collections.singletonMap("message", "이 이메일은 이미 사용 중입니다.")
            );
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "이 이메일은 사용 가능합니다."));
    }

}
