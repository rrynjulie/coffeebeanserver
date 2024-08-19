package com.lec.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    // 이메일 서비스 설정을 관리하는 클래스
    // 메일 서버 설정값을 application.yml 에서 가지고오기
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean starttlsRequired;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private int connectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private int writeTimeout;

    // 이메일 전송 기본설정
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());

        return mailSender;
    }

    // starttls 는 인터넷 통신 프로토콜에서 암호화를 추가하는 방식중 하나.
    // starttls 명령이 실행된 이후에는 연결이 암호화로 전환된다.

    // 데이터의 안전성 보장
    // 서버와의 호환성 유지

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth); // smtp 인증사용
        properties.put("mail.smtp.starttls.enable", starttlsEnable); // starttls 사용 설정
        properties.put("mail.smtp.starttls.required", starttlsRequired); // starttls 필수 사용 설정
        properties.put("mail.smtp.connectiontimeout", connectionTimeout); // 연결 타임아웃 설정
        properties.put("mail.smtp.timeout", timeout); // 응답 타임아웃 설정
        properties.put("mail.smtp.writetimeout", writeTimeout); // 데이터 전송 타임아웃 설정

        return properties;
    }
}
