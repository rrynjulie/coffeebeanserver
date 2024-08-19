package com.lec.spring.repository;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 유저 id 로 참여하고 있는 채팅방 조회
    List<ChatRoom> findByBuyerIdUserIdOrSellerIdUserId(Long buyerId, Long sellerId);

    // review 사용
    ChatRoom findByChatRoomId(Long chatRoomId);
    Product findProductByChatRoomId(Long chatRoomId);
}