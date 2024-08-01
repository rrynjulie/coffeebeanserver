package com.lec.spring.controller;

import com.lec.spring.domain.Review;
import com.lec.spring.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/review/write")
    public ResponseEntity<?> create(@RequestBody Review review) {
        return new ResponseEntity<>(reviewService.create(review), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/review/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(reviewService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/review/detail/{reviewId}")
    public ResponseEntity<?> readOne(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.readOne(reviewId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/review/update")
    public ResponseEntity<?> update(@RequestBody Review review) {
        return new ResponseEntity<>(reviewService.update(review), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/review/delete/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.delete(reviewId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}