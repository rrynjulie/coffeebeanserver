package com.lec.spring.controller;

import com.lec.spring.domain.SampleReview;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserJoinDTO;
import com.lec.spring.service.SampleReviewService;
import com.lec.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Autowired
    private SampleReviewService sampleReviewService;

    @Value("${app.oauth2.kakao.user-info-uri}")
    private String userInfoUri;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public String join(@RequestBody UserJoinDTO userJoinDTO) {
        User user = User.builder()
                .userName(userJoinDTO.getUserName())
                .password(userJoinDTO.getPassword())
                .nickName(userJoinDTO.getNickName())
                .email(userJoinDTO.getEmail())
                .build();
        user = userService.join(user);
        if (user == null) return "JOIN FAILED";
        return "JOIN OK : " + user;
    }

    @GetMapping("/sampleReview/{userId}")
    public ResponseEntity<Map<String, Integer>> getSampleReviewCounts(@PathVariable Long userId) {
        Map<String, Integer> counts = sampleReviewService.getSampleReviewCountsByUserId(userId);
        return ResponseEntity.ok(counts);
    }

}