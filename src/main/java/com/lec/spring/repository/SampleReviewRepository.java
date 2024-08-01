package com.lec.spring.repository;

import com.lec.spring.domain.SampleReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleReviewRepository extends JpaRepository<SampleReview, Long> {
}