package com.lec.spring.controller;

import com.lec.spring.domain.*;
import com.lec.spring.service.ChatRoomService;
import com.lec.spring.service.ProductService;
import com.lec.spring.service.ReviewService;
import com.lec.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/detail/{chatRoomId}/{writerId}")
    public ResponseEntity<?> readByWriterIdAndChatRoomId(@PathVariable Long chatRoomId, @PathVariable Long writerId) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
        User writer = userService.findByUserId(writerId);

        Review review = reviewService.findReviewByChatRoomAndWriter(chatRoom, writer);
        Product product = reviewService.findProductByChatRoomId(chatRoomId);

        Map<String, Object> response = new HashMap<>();
        response.put("review", review);
        response.put("product", product);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/list/writer/{userId}")
    public ResponseEntity<?> readWriterReviewAll(@PathVariable Long userId){
        List<Review> reviews = reviewService.readWriterReviewAll(userId);
        List<Map<String, Object>> reviewProductList = new ArrayList<>();

        for (Review review : reviews) {
            ChatRoom chatRoom = reviewService.findChatRoomByReviewId(review.getReviewId());
            Product product = chatRoomService.findProductByChatRoomId(chatRoom.getChatRoomId());

            Map<String, Object> reviewProductMap = new HashMap<>();
            reviewProductMap.put("review", review);
            reviewProductMap.put("product", product);

            reviewProductList.add(reviewProductMap);
        }

        return new ResponseEntity<>(reviewProductList, HttpStatus.OK);
    }

        @GetMapping("/list/recipient/{userId}")
    public ResponseEntity<?> readRecipientReviewAll(@PathVariable Long userId){
        return new ResponseEntity<>(reviewService.readRecipientReviewAll(userId), HttpStatus.OK);
    }

    @GetMapping("/checkInfo/{chatRoomId}/{writerId}")
    public ResponseEntity<?> checkChatRoomInfo(@PathVariable Long chatRoomId, @PathVariable Long writerId) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
        User writer = userService.findByUserId(writerId);
        User recipient;
        if(writerId == chatRoom.getSellerId().getUserId()){
            recipient = userService.findByUserId(chatRoom.getBuyerId().getUserId());
        } else {
            recipient = userService.findByUserId(chatRoom.getSellerId().getUserId());
        };

        Product product = reviewService.findProductByChatRoomId(chatRoomId);

        Map<String, Object> response = new HashMap<>();
        response.put("writer", writer);
        response.put("recipient", recipient);
        response.put("product", product);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}