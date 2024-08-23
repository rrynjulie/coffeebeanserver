package com.lec.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {  // corsConfigurer 메소드를 정의하여 WebMvcConfigurer 반환
        return new WebMvcConfigurer() {     // WebMvcConfigurer 인터페이스를 구현하는 익명 클래스 생성
            @Override
            public void addCorsMappings(CorsRegistry registry) {    // addCorsMappings 메소드를 오버라이딩하여 CORS 설정 추가.
                registry.addMapping("/**")      // 모든 경로에 CORS 설정 적용
                        .allowedOrigins("http://localhost:3000", "http://43.202.202.236:3000")    // 3000포트에 대한 요청 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용 HTTP 메소드 지정
                        .allowedHeaders("*")    // 모든 헤더 허용
                        .allowCredentials(true);    // 쿠키, HTTP 등 인증정보 요청 허용
            }
        };
    }

}
