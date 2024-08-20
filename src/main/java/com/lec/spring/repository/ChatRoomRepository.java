package com.lec.spring.repository;

import com.lec.spring.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 사용자 ID로 참여하고 있는 채팅방 조회
    @Query("SELECT cr FROM chat_room cr LEFT JOIN FETCH cr.product WHERE cr.buyerId.userId = :userId OR cr.sellerId.userId = :userId")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);

    // 채팅방 ID로 조회
    Optional<ChatRoom> findByChatRoomId(Long chatRoomId);
}
