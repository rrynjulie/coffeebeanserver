package com.lec.spring.controller;

import com.lec.spring.service.SampleReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SampleReviewController {
    private final SampleReviewService sampleReviewService;
}