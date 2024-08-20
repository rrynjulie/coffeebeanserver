package com.lec.spring.repository;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 유저 id 로 참여하고 있는 채팅방 조회
    @Query("SELECT cr FROM chat_room cr LEFT JOIN FETCH cr.product WHERE cr.buyerId.userId = :userId OR cr.sellerId.userId = :userId")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);

    // review 사용
    ChatRoom findByChatRoomId(Long chatRoomId);
    @Query("SELECT c.product FROM chat_room c WHERE c.chatRoomId = :chatRoomId")
    Product findProductByChatRoomId(Long chatRoomId);
}