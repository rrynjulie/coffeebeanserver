package com.lec.spring.repository;

import com.lec.spring.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // 어떤 유저가 쓴 메세지인지
    List<Message> findByUserId(Long userId);

    // 메세지가 어떤 채팅방에 속해있는지
    List<Message> findByChatRoomId(Long chatRoomId);

}