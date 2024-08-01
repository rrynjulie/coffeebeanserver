package com.lec.spring.service;

import com.lec.spring.domain.Review;
import com.lec.spring.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    // 기본적인 CRUD
    @Transactional
    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Review readOne(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Review> readAll() {
        return reviewRepository.findAll();
    }

    @Transactional
    public Review update(Review review) {
        Review reviewEntity = reviewRepository.findById(review.getReviewId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return reviewEntity;
    }

    @Transactional
    public String delete(Long reviewId) {
        reviewRepository.deleteById(reviewId);
        return "ok";
    }

    // 추가 기능
    // TODO
}