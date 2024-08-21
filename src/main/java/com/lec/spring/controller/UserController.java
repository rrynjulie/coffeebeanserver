package com.lec.spring.controller;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.SampleReview;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserJoinDTO;
import com.lec.spring.repository.CarRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.service.AttachmentService;
import com.lec.spring.service.SampleReviewService;
import com.lec.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private AttachmentService attachmentService;

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

    @PostMapping("/profileUpload/{userId}")
    public ResponseEntity<?> profileUpload(
            @PathVariable Long userId,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        User user = userService.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
        }

        if (file != null) {
            // 기존 파일이 있는지 확인하고 삭제
            List<Attachment> existingAttachments = attachmentService.findByUser(user);
            if (!existingAttachments.isEmpty()) {
                for (Attachment attachment : existingAttachments) {
                    attachmentService.delete(attachment.getAttachmentId());
                }
            }
            // 새 파일 업로드
            attachmentService.addFile(file, user);
        }

        return ResponseEntity.ok("유저 id: " + userId + "에 성공적으로 프로필이 업로드 되었습니다.");
    }

    // 프로필 이미지 가져오기
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfileImage(@PathVariable Long userId) {
        User user = userService.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
        }

        List<Attachment> attachments = attachmentService.findByUser(user);
        if (attachments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로필 이미지가 없습니다.");
        }

        String profileImageUrl = attachments.get(0).getSource();
        return ResponseEntity.ok(profileImageUrl);
    }

    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<?> deleteProfileImage(@PathVariable Long userId) {
        User user = userService.findByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
        }

        // 해당 유저의 기존 프로필 이미지를 찾기
        List<Attachment> attachments = attachmentService.findByUser(user);
        if (attachments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("프로필 이미지가 없습니다.");
        }

        // 이미지 삭제
        for (Attachment attachment : attachments) {
            attachmentService.delete(attachment.getAttachmentId());
        }

        return ResponseEntity.ok("유저 id: " + userId + "의 프로필 이미지가 삭제되었습니다.");
    }
}