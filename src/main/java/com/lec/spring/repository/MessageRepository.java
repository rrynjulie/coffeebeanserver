package com.lec.spring.repository;

import com.lec.spring.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM message m WHERE m.sender.userId = :senderId")
    List<Message> findBySenderId(@Param("senderId") Long senderId);

    List<Message> findByChatRoomChatRoomId(Long chatRoomId);

    @Query("SELECT m FROM message m WHERE m.chatRoom.chatRoomId = :chatRoomId ORDER BY m.sendTime DESC")
    List<Message> findLastMessageByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM message m WHERE m.chatRoom.chatRoomId = :chatRoomId AND m.IsRead = false AND m.sender.userId <> :currentUserId")
    Long unreadMessage(@Param("chatRoomId") Long chatRoomId, @Param("currentUserId") Long currentUserId);
}
