package com.lec.spring.repository;

import com.lec.spring.domain.Email;
import com.lec.spring.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<User, Long> {

    // 인증코드 발송한 이메일 주소 조회
    public Optional<User> findByEmail(String email);
}
