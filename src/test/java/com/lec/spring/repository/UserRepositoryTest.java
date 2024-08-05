package com.lec.spring.repository;

import com.lec.spring.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void registerTest() {
        User user1 = User.builder()
                .userName("admin3")
                .nickName("admin3")
                .password(passwordEncoder.encode("1234"))
                .email("admin3.@mail.com")
                .regDate(LocalDateTime.now())
                .reliability(500)
                .role("ROLE_USER,ROLE_ADMIN")
                .build();

//        User user2 = User.builder()
//                .userName("user2")
//                .nickName("2")
//                .password(passwordEncoder.encode("1234"))
//                .email("2.@mail.com")
//                .regDate(LocalDateTime.now())
//                .reliability(500)
//                .role("ROLE_USER")
//                .build();

//        User user2 = User.builder()
//                .userName("user2".toUpperCase())
//                .password(passwordEncoder.encode("1234"))
//                .nickName("2")
//                .email("2.@mail.com")
//                .reliability(500)
//                .build();
//
//
//        User admin1 = User.builder()
//                .userName("admin1".toUpperCase())
//                .password(passwordEncoder.encode("1234"))
//                .role("ROLE_USER,ROLE_ADMIN")
//                .nickName("3")
//                .email("3.@mail.com")
//                .reliability(500)
//                .build();


        userRepository.saveAllAndFlush(List.of(user1));
    }
}