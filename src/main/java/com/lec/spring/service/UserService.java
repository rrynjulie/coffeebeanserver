package com.lec.spring.service;

import com.lec.spring.domain.User;
import com.lec.spring.jwt.JWTUtil;
import com.lec.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    @Value("${app.oauth2.password}")
    private String oauth2Password;   // OAuth2 회원 가입시 기본 PW

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
        User existingUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        existingUser.setNickName(user.getNickName());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    @Transactional
    public String delete(Long userId) {
        userRepository.deleteById(userId);
        return "ok";
    }

    // 로그인 시작 -----------------------------------------------

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 사용자 가입 및 로그인
    public User join(User user){
        String username = user.getUserName();
        String password = user.getPassword();

        if(userRepository.existsByUserName(username)){
            return null;  // 회원 가입 실패
        }
        user.setUserName(user.getUserName().toUpperCase());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }


    public User findByUsername(String username){
        return userRepository.findByUserName(username.toUpperCase());
    }
    public User findByUserId(Long userId){
        return userRepository.findByUserId(userId);
    }

    // 카카오 사용자 등록 또는 인증
    @Transactional
    public User registerOrAuthenticateKakaoUser(String kakaoId, String nickname, String email) {
        String username = "KAKAO_" + kakaoId;
        User user = userRepository.findByUserName(username);

        if (user == null) {
            user = new User();
            user.setUserName(username);
            user.setNickName(nickname);
            user.setPassword(passwordEncoder.encode(oauth2Password)); // 기본 패스워드 설정
            user.setRole("ROLE_USER");
            user.setProvider("KAKAO");
            user.setProviderId(kakaoId);

            // 이메일이 없으면 임의의 이메일 생성
            if (email == null || email.isEmpty()) {
                email = "noemail_" + kakaoId + "@kakao.com";
            }
            user.setEmail(email);

            user = userRepository.save(user);
        }

        return user;
    }

    // JWT 생성 및 반환
    public String generateJwtToken(User user) {
        return jwtUtil.createJwt(
                user.getUserId(),
                user.getUserName(),
                user.getNickName(),
                user.getEmail(),
                user.getRegDate().toString(),
                user.getReliability(),
                user.getRole(),
                1000L * 60 * 60 // 토큰 유효기간 1시간 설정
        );
    }

    //  로그인 끝 ------------------------------------------------


    // 추가 기능
    // TODO
}