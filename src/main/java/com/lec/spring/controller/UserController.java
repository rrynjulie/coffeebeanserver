package com.lec.spring.controller;

import com.lec.spring.domain.SampleReview;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserJoinDTO;
import com.lec.spring.repository.CarRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.service.SampleReviewService;
import com.lec.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Autowired
    private SampleReviewService sampleReviewService;
    @Autowired
    private UserRepository userRepository;


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

    @CrossOrigin
    @GetMapping("/sampleReview/{userId}")
    public ResponseEntity<Map<String, Integer>> getSampleReviewCounts(@PathVariable Long userId) {
        Map<String, Integer> counts = sampleReviewService.getSampleReviewCountsByUserId(userId);
        return ResponseEntity.ok(counts);
    }


    @PostMapping("/reliabilityUpdate/{userId}")
    public ResponseEntity<?> reliabilityUpdate(@RequestParam String rating, @PathVariable Long userId) {
        User user = userService.findByUserId(userId);

        if (rating.equals("Best")) {
            user.setReliability((user.getReliability() + 1));
        } else if (rating.equals("Good")) {
            user.setReliability((user.getReliability() + 0));
        } else if (rating.equals("Bad")) {
            user.setReliability((user.getReliability() - 1));
        }

        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}