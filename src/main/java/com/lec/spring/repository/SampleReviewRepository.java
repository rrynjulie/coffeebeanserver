package com.lec.spring.repository;

import com.lec.spring.domain.SampleReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SampleReviewRepository extends JpaRepository<SampleReview, Long> {
    List<SampleReview> findByUser_UserId(Long userId);
}