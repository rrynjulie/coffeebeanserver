package com.lec.spring.controller;

import com.lec.spring.domain.SampleReview;
import com.lec.spring.service.SampleReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class SampleReviewController {
    private final SampleReviewService sampleReviewService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/sampleReview/write")
    public ResponseEntity<?> create(@RequestBody SampleReview sampleReview) {
        return new ResponseEntity<>(sampleReviewService.create(sampleReview), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/sampleReview/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(sampleReviewService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/sampleReview/delete/{sampleReviewId}")
    public ResponseEntity<?> delete(@PathVariable Long sampleReviewId) {
        return new ResponseEntity<>(sampleReviewService.delete(sampleReviewId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}