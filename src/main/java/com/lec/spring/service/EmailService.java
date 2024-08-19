package com.lec.spring.service;

import com.lec.spring.repository.EmailRepository;
import com.lec.spring.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Random random = new Random();
    private final RedisUtil redisUtil;
    private final EmailRepository emailRepository;

    @Value("${spring.mail.username}")
    private String from;

    // 이메일 발송 코드
    public String sendVerificationCode(String to) {
        String code = generateVerificationCode();
        redisUtil.setDataExpire(to, code, 300); // 5분
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 발신자 설정
        message.setTo(to); // 수신자 설정
        message.setSubject("CoffeeBean 인증코드입니다.");
        message.setText("인증 코드는 : " + code);
        mailSender.send(message); // 이메일 발송
        return code;
    }

    // 인증번호 생성
    private String generateVerificationCode() {
        return String.valueOf(100000 + random.nextInt(900000)); // 999999 사이의 랜덤 숫자 발송
    }

    // 이메일과 코드를 검증
    // Redis 에 저장된 번호와 일치하는지 확인
    public boolean verifyEmailCode(String email, String code) {
        String storedCode = redisUtil.getData(email); // Redis에서 저장된 코드 가져오기
        if (storedCode != null && storedCode.equals(code)) {
            redisUtil.deleteData(email); // 인증 성공 후 Redis에서 코드 삭제
            return true;
        }
        return false;
    }

    // 이메일 중복 검증
    public boolean isEmailRegistered(String email) {
        System.out.println("Checking email: " + email);
        boolean isPresent = emailRepository.findByEmail(email.toLowerCase()).isPresent();
        System.out.println("Email found: " + isPresent);
        return isPresent;
    }
}
