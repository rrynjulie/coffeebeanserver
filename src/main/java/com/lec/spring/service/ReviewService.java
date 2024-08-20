package com.lec.spring.service;

import com.lec.spring.domain.*;
import com.lec.spring.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final SampleReviewService sampleReviewService;

    // 기본적인 CRUD
    @Transactional
    public Review create(Review review, Long chatRoomId, Long writerId, SampleReview sampleReview) {
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId).orElseThrow(() -> new IllegalArgumentException("해당 채팅이 존재하지 않습니다."));

        if (!chatRoom.getDealComplete()) {
            throw new IllegalArgumentException("거래가 완료되지 않았습니다.");
        }
        User writer = userService.findByUserId(writerId);

        review.setChatRoom(chatRoom);
        review.setRegDate(LocalDateTime.now());
        review.setWriter(writer);

        if (chatRoom.getSellerId().getUserId().equals(writerId)) {
            review.setRecipient(chatRoom.getBuyerId());
        } else if (chatRoom.getBuyerId().getUserId().equals(writerId)){
            review.setRecipient(chatRoom.getSellerId());
        } else {
            throw new IllegalArgumentException("작성자는 채팅방의 구매자 또는 판매자여야 합니다.");
        }

        Review savedReview = reviewRepository.save(review);

        sampleReview.setUser(savedReview.getRecipient());
        sampleReview.setReview(savedReview);
        sampleReviewService.create(sampleReview);

        return savedReview;
    }

    @Transactional(readOnly = true)
    public Review findReviewByChatRoomAndWriter(ChatRoom chatRoom, User writer) {
        return reviewRepository.findByChatRoomAndWriter(chatRoom, writer);
    }

    @Transactional(readOnly = true)
    public Product findProductByChatRoomId(Long chatRoomId) {
        return chatRoomService.findProductByChatRoomId(chatRoomId);
    }

    @Transactional(readOnly = true)
    public List<Review> readWriterReviewAll(Long writerId) {
        return reviewRepository.findByWriterUserId(writerId);
    }
    @Transactional(readOnly = true)
    public List<Review> readRecipientReviewAll(Long recipientId) {
        return reviewRepository.findByRecipientUserId(recipientId);
    }

    @Transactional
    public String delete(Long reviewId) {
        sampleReviewService.delete(reviewId);
        reviewRepository.deleteById(reviewId);
        return "ok";
    }

    public ChatRoom findChatRoomByReviewId(Long reviewId){
        return reviewRepository.findChatRoomByReviewId(reviewId);
    }
}