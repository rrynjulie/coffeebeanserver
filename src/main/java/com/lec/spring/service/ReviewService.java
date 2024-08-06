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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ChatRoomService chatRoomService;

    // 기본적인 CRUD
    @Transactional
    public Review create(Review review, Long chatRoomId, Long writerId) {
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId).orElseThrow(() -> new IllegalArgumentException("해당 채팅이 존재하지 않습니다."));

        if (!chatRoom.getDealComplete()) {
            throw new IllegalArgumentException("거래가 완료되지 않았습니다.");
        }

        review.setChatRoom(chatRoom);
        review.setRegDate(LocalDateTime.now());

        if (chatRoom.getSellerId().equals(writerId)) {
            review.setWriter(chatRoom.getSellerId());
            System.out.println("-".repeat(40));
            System.out.println(chatRoom.getSellerId());
            System.out.println("-".repeat(40));
            System.out.println(chatRoom.getBuyerId());
            review.setRecipient(chatRoom.getBuyerId());
        } else if (chatRoom.getBuyerId().equals(writerId)){
            review.setWriter(chatRoom.getBuyerId());
            review.setRecipient(chatRoom.getSellerId());
        }

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<Review> readOne(ChatRoom chatRoom, User user) {
        return reviewRepository.findByChatRoomAndWriter(chatRoom, user);
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