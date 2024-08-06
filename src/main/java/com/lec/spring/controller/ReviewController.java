package com.lec.spring.controller;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Review;
import com.lec.spring.domain.User;
import com.lec.spring.service.ChatRoomService;
import com.lec.spring.service.ReviewService;
import com.lec.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    // 기본적인 CRUD
    @PostMapping("/review/write/{chatRoomId}/{writerId}")
    public ResponseEntity<?> create(@RequestBody Review review, @PathVariable Long chatRoomId, @PathVariable Long writerId) {
        return new ResponseEntity<>(reviewService.create(review, chatRoomId, writerId), HttpStatus.CREATED);
    }

    @GetMapping("/review/list/{chatRoomId}/{writerId}")
    public ResponseEntity<?> readAllByUserId(@PathVariable Long chatRoomId, @PathVariable Long writerId) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(chatRoomId);
        User user = userService.findByUserId(writerId);
        return new ResponseEntity<>(reviewService.readOne(chatRoom, user), HttpStatus.OK);
    }

    @DeleteMapping("/review/delete/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.delete(reviewId), HttpStatus.OK);
    }
}