package com.lec.spring.repository;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 사용자 ID로 참여하고 있는 채팅방 조회
    @Query("SELECT cr FROM chat_room cr LEFT JOIN FETCH cr.product WHERE cr.buyerId.userId = :userId OR cr.sellerId.userId = :userId")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);

    @Query("SELECT cr FROM chat_room cr " +
            "LEFT JOIN FETCH cr.buyerId " +
            "LEFT JOIN FETCH cr.sellerId " +
            "WHERE cr.chatRoomId = :chatRoomId")
    Optional<ChatRoom> findByBuyerAndSeller(@Param("chatRoomId") Long chatRoomId);

    // 채팅방 ID로 조회
    Optional<ChatRoom> findByChatRoomId(Long chatRoomId);
    ChatRoom findChatRoomByChatRoomId(Long chatRoomId);

    @Query("SELECT cr FROM chat_room cr WHERE cr.product.productId = :productId")
    List<ChatRoom> findByProductId(@Param("productId") Long productId);
    @Query("SELECT c.product FROM chat_room c WHERE c.chatRoomId = :chatRoomId")
    Product findProductByChatRoomId(Long chatRoomId);

    List<ChatRoom> findByBuyerId_UserId(Long userId);
}
