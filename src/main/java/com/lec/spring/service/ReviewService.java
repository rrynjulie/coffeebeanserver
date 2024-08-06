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
    private final UserService userService;

    // 기본적인 CRUD
    @Transactional
    public Review create(Review review, Long chatRoomId, Long writerId) {
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

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<Review> readOne(ChatRoom chatRoom, User user) {
        return reviewRepository.findByChatRoomAndWriter(chatRoom, user);
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
        reviewRepository.deleteById(reviewId);
        return "ok";
    }

    // 추가 기능
    // TODO
}