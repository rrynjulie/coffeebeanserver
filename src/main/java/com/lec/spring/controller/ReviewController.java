package com.lec.spring.controller;

import com.lec.spring.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final ReviewService reviewService;
}