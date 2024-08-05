package com.lec.spring.service;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Review;
import com.lec.spring.domain.User;
import com.lec.spring.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ChatRoomService chatRoomService;

    // 기본적인 CRUD
    @Transactional
    public Review create(Review review, Long chatRoomId, boolean isBuyer) {

        ChatRoom chatRoom = chatRoomService.findById(chatRoomId).orElseThrow(() -> new IllegalArgumentException("해당 채팅이 존재하지 않습니다."));

        if (!chatRoom.getDealComplete()) {
            throw new IllegalArgumentException("거래가 완료되지 않았습니다.");
        }

        review.setChatRoom(chatRoom);
        review.setRegDate(LocalDateTime.now());
        review.setIsBuyerReview(isBuyer);

        if (isBuyer) {
            review.setBuyerId(chatRoom.getBuyerId());
            review.setSellerId(chatRoom.getSellerId());
        } else {
            review.setSellerId(chatRoom.getSellerId());
            review.setBuyerId(chatRoom.getBuyerId());
        }

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
        reviewEntity.setContent(review.getContent());
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