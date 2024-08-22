package com.lec.spring.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.User;
import com.lec.spring.domain.oauth.KakaoOAuthToken;
import com.lec.spring.domain.oauth.KakaoProfile;
import com.lec.spring.jwt.JWTUtil;
import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    private JWTUtil jwtUtil;

    // 카카오 OAuth 설정값을 주입합니다.
    @Value("${app.oauth2.kakao.client-id}")
    private String kakaoClientId;

    @Value("${app.oauth2.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${app.oauth2.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${app.oauth2.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    // OAuth 사용자를 위한 고정된 비밀번호 (보안적으로 안전하게 관리해야 합니다)
    @Value("${app.oauth2.password}")
    private String oauth2Password;

    @Autowired
    private UserService userService ;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 카카오로부터 인증 코드를 받은 후 처리하는 엔드포인트
    @PostMapping ("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestBody Map<String, String> requestData) {
        String code = requestData.get("code");
        System.out.println("받은 code : " + code);

        // 인증 코드를 사용하여 액세스 토큰을 요청합니다.
        KakaoOAuthToken token = kakaoAccessToken(code);

        // 받은 토큰으로 사용자 정보를 요청합니다.
        KakaoProfile profile = kakaoUserInfo(token.getAccessToken());

        // 사용자 정보를 기반으로 사용자 등록 또는 로그인을 진행합니다.
        User kakaoUser = registerKakaoUser(profile);

        loginKakaoUser(kakaoUser);

        // JWT 생성
        String jwt = jwtUtil.createJwt(
                kakaoUser.getUserId(),
                kakaoUser.getUserName(),
                kakaoUser.getNickName(),
                kakaoUser.getEmail(),
                kakaoUser.getRegDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                kakaoUser.getReliability(),
                kakaoUser.getRole(),
                1000 * 60 * 60 * 10L // 10시간 유효
        );

//      // 사용자 정보와 JWT를 JSON 형태로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("user", kakaoUser);
        response.put("accessToken", jwt);

        System.out.println("kakaoCallBack 메소드 종료");
        return ResponseEntity.ok(response);
    }

    private boolean isCodeUsed(String code) {
        // 인증 코드가 이미 사용되었는지 확인하는 로직 추가
        // 예: 데이터베이스 조회
        return false; // 이미 사용된 경우 true 반환
    }

    // 카카오 액세스 토큰 요청 메소드
    private KakaoOAuthToken kakaoAccessToken(String code) {

        if (isCodeUsed(code)) {
            throw new RuntimeException("이 인증 코드는 이미 사용되었습니다.");
        }

        System.out.println("카카오 엑세스 토큰 요청 메소드 시작");
        System.out.println("받은 코드: " + code);
        System.out.println("카카오 클라이언트 ID: " + kakaoClientId);
        System.out.println("리다이렉트 URI: " + kakaoRedirectUri);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoTokenUri, HttpMethod.POST, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoOAuthToken token;
        try {
            token = objectMapper.readValue(response.getBody(), KakaoOAuthToken.class);
            System.out.println("kakaoAccessToken 메소드 성공적으로 토큰 획득");
        } catch (JsonProcessingException e) {
            System.out.println("kakaoAccessToken 메소드에서 토큰 파싱 실패: " + e.getMessage());
            throw new RuntimeException("토큰 응답 파싱 실패", e);
        }

        System.out.println("kakaoAccessToken 메소드 종료");
        return token;
    }

    // 카카오 사용자 정보 요청 메소드
    private KakaoProfile kakaoUserInfo(String accessToken) {
        System.out.println("카카오 사용자 정보 요청 메소드 시작");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoUserInfoUri, HttpMethod.POST, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile profile;
        try {
            profile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
            System.out.println("kakaoUserInfo 메소드에서 사용자 정보 성공적으로 획득");
        } catch (JsonProcessingException e) {
            System.out.println("kakaoUserInfo 메소드에서 사용자 정보 파싱 실패");
            throw new RuntimeException("사용자 정보 응답 파싱 실패", e);
        }

        System.out.println("kakaoUserInfo 메소드 종료");
        return profile;
    }

    // 카카오 사용자를 내부 시스템에 등록하는 메소드
    private User registerKakaoUser(KakaoProfile profile) {
        System.out.println("카카오 사용자를 내부 시스템에 등록하는 메소드 시작");
        String provider = "KAKAO";
        String providerId = profile.getId().toString();
        String username = provider + "_" + providerId;  // 카카오 사용자 고유 ID를 이용해 유니크한 username 생성
        String nickname = profile.getKakaoAccount().getProfile().getNickname();  // 카카오에서 제공하는 닉네임

        // 이미 등록된 사용자인지 확인
        User existingUser = userService.findByUsername(username);
        if (existingUser == null) {
            // 미등록 사용자인 경우 새로 등록
            User newUser = User.builder()
                    .userName(username)
                    .nickName(nickname)
                    .password(oauth2Password)  // OAuth 로그인의 경우, 설정된 고정 비밀번호 사용
                    .provider(provider)
                    .providerId(providerId)
                    .email(null)  // 이메일 아무거나 지정.
                    .role("ROLE_USER")  // 기본 권한 설정
                    .build();

            System.out.println("registerKakaoUser 메소드에서 새 사용자 등록 완료");
            return userService.join(newUser); // userService의 join 메소드 사용
        }

        System.out.println("registerKakaoUser 메소드에서 기존 사용자 반환");
        return existingUser;
    }

    // 카카오 사용자 로그인 처리 메소드
    private void loginKakaoUser(User kakaoUser) {
        System.out.println("loginKakaoUser 메소드 시작");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                kakaoUser.getUserName(), oauth2Password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        System.out.println("카카오 인증 로그인 처리 완료");
        System.out.println("loginKakaoUser 메소드 종료");
    }
}
