package com.lec.spring.repository;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Review;
import com.lec.spring.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByChatRoomAndWriter(ChatRoom chatRoom, User writer);
    List<Review> findByWriterUserId(Long writerId);
    List<Review> findByRecipientUserId(Long recipientId);
    ChatRoom findChatRoomByReviewId(Long chatRoomId);
}