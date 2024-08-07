package com.lec.spring.repository;

import com.lec.spring.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // 어떤 유저가 쓴 메세지인지
    List<Message> findBySenderUserId(Long userId);

    // 채팅방에 어떤 메세지가 속해있는지
    List<Message> findByChatRoomChatRoomId(Long chatRoomId);

    // 각 채팅방의 마지막 메세지를 가져오는 쿼리
    @Query("SELECT m FROM message m WHERE m.chatRoom.chatRoomId = :chatRoomId ORDER BY m.sendTime DESC LIMIT 1")
    Message findLastMessageByChatRoomId(Long chatRoomId);

    // 각 채팅방의 상대방이 보낸 안읽은 메세지 개수
    @Query("SELECT COUNT(m) FROM message m WHERE m.chatRoom.chatRoomId = :chatRoomId AND m.IsRead = false AND m.sender.userId <> :currentUserId")
    // :chatRoomId, :currentUserId ---> 파라미터로 전달하는 값 / <> ---> 같지 않다
    Long unreadMessage(@Param("chatRoomId") Long chatRoomId, @Param("currentUserId") Long currentUserId);

}