package com.lec.spring.controller;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Review;
import com.lec.spring.domain.SampleReview;
import com.lec.spring.domain.User;
import com.lec.spring.service.ChatRoomService;
import com.lec.spring.service.ReviewService;
import com.lec.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("review")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    // 기본적인 CRUD
//    @PostMapping("/write/{chatRoomId}/{writerId}")
//    public ResponseEntity<?> create(@RequestBody Review review, @PathVariable Long chatRoomId, @PathVariable Long writerId, @RequestBody SampleReview sampleReview) {
//        return new ResponseEntity<>(reviewService.create(review, chatRoomId, writerId, sampleReview), HttpStatus.CREATED);
//    }
    @PostMapping("/write/{chatRoomId}/{writerId}")
    public ResponseEntity<?> createReview(
            @PathVariable("chatRoomId") Long chatRoomId,
            @PathVariable("writerId") Long writerId,
            @RequestBody Map<String, Object> reviewData) {

        Review review = new Review();
        review.setContent((String) reviewData.get("reviewContent"));

        SampleReview sampleReview = new SampleReview();
        sampleReview.setManner((Integer) reviewData.get("manner"));
        sampleReview.setResponse((Integer) reviewData.get("response"));
        sampleReview.setTime((Integer) reviewData.get("time"));
        sampleReview.setBadManner((Integer) reviewData.get("badManner"));

        Review createdReview = reviewService.create(review, chatRoomId, writerId, sampleReview);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }


    @GetMapping("/list/{chatRoomId}/{writerId}")
    public ResponseEntity<?> readAllByUserId(@PathVariable Long chatRoomId, @PathVariable Long writerId) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
        User user = userService.findByUserId(writerId);
        return new ResponseEntity<>(reviewService.readOne(chatRoom, user), HttpStatus.OK);
    }

    @GetMapping("/list/writer/{userId}")
    public ResponseEntity<?> readWriterReviewAll(@PathVariable Long userId){
        return new ResponseEntity<>(reviewService.readWriterReviewAll(userId), HttpStatus.OK);
    }

    @GetMapping("/list/recipient/{userId}")
    public ResponseEntity<?> readRecipientReviewAll(@PathVariable Long userId){
        return new ResponseEntity<>(reviewService.readRecipientReviewAll(userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.delete(reviewId), HttpStatus.OK);
    }
}