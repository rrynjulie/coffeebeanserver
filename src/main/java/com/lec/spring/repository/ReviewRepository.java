package com.lec.spring.repository;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Review;
import com.lec.spring.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByChatRoomAndWriter(ChatRoom chatRoom, User writer);
    List<Review> findByWriterUserId(Long writerId);
    List<Review> findByRecipientUserId(Long recipientId);
    @Query("SELECT r.chatRoom FROM review r WHERE r.reviewId = :reviewId")
    ChatRoom findChatRoomByReviewId(@Param("reviewId") Long reviewId);

}