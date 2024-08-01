package com.lec.spring.service;

import com.lec.spring.domain.SampleReview;
import com.lec.spring.repository.SampleReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SampleReviewService {
    private final SampleReviewRepository sampleReviewRepository;

    // 기본적인 CRUD
    @Transactional
    public SampleReview create(SampleReview sampleReview) {
        return sampleReviewRepository.save(sampleReview);
    }

    @Transactional(readOnly = true)
    public SampleReview readOne(Long sampleReviewId) {
        return sampleReviewRepository.findById(sampleReviewId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<SampleReview> readAll() {
        return sampleReviewRepository.findAll();
    }

    @Transactional
    public SampleReview update(SampleReview sampleReview) {
        SampleReview sampleReviewEntity = sampleReviewRepository.findById(sampleReview.getSampleReviewId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return sampleReviewEntity;
    }

    @Transactional
    public String delete(Long sampleReviewId) {
        sampleReviewRepository.deleteById(sampleReviewId);
        return "ok";
    }

    // 추가 기능
    // TODO
}