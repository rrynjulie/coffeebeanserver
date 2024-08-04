package com.lec.spring.repository;

import com.lec.spring.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 유저 id 로 참여하고 있는 채팅방 조회
    List<ChatRoom> findByBuyerIdUserIdOrSellerIdUserId(Long buyerId, Long sellerId);

}