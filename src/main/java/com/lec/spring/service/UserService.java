package com.lec.spring.service;

import com.lec.spring.domain.User;
import com.lec.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    // 기본적인 CRUD
    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User readOne(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User update(User user) {
        User userEntity = userRepository.findById(user.getUserId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return userEntity;
    }

    @Transactional
    public String delete(Long userId) {
        userRepository.deleteById(userId);
        return "ok";
    }

    // 추가 기능
    // TODO
}