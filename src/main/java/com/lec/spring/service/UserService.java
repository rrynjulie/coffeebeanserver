package com.lec.spring.service;

import com.lec.spring.domain.User;
import com.lec.spring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

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

    // 로그인 시작 -----------------------------------------------

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User join(User user){
        String username = user.getUserName();
        String password = user.getPassword();


        if(userRepository.existsByUserName(username)){
            return null;  // 회원 가입 실패
        }


        user.setUserName(user.getUserName().toUpperCase());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_MEMBER");  // 기본적으로 MEMBER
        return userRepository.save(user);
    }


    public User findByUsername(String username){
        return userRepository.findByUserName(username.toUpperCase());
    }


    //  로그인 끝 ------------------------------------------------


    // 추가 기능
    // TODO
}