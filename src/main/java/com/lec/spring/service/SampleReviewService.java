package com.lec.spring.service;

import com.lec.spring.domain.SampleReview;
import com.lec.spring.repository.SampleReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SampleReviewService {
    private final SampleReviewRepository sampleReviewRepository;

    // 기본적인 CRUD
    @Transactional
    public SampleReview create(SampleReview sampleReview) {
        return sampleReviewRepository.save(sampleReview);
    }

    @Transactional
    public String delete(Long sampleReviewId) {
        sampleReviewRepository.deleteById(sampleReviewId);
        return "ok";
    }

    public Map<String, Integer> getSampleReviewCountsByUserId(Long userId) {
        List<SampleReview> reviews = sampleReviewRepository.findByUser_UserId(userId);

        Map<String, Integer> counts = new HashMap<>();
        counts.put("mannerCount", (int) reviews.stream().filter(r -> r.getManner() == 1).count());
        counts.put("responseCount", (int) reviews.stream().filter(r -> r.getResponse() == 1).count());
        counts.put("timeCount", (int) reviews.stream().filter(r -> r.getTime() == 1).count());
        counts.put("badMannerCount", (int) reviews.stream().filter(r -> r.getBadManner() == 1).count());

        return counts;
    }
}