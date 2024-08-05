package com.lec.spring.service;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    // 새로운 채팅방 생성 및 데이터 베이스 저장
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    // 사용자가 참여하고 있는 모든 채팅방 조회
    public List<ChatRoom> findByUserId(Long userId) {
        return chatRoomRepository.findByBuyerIdUserIdOrSellerIdUserId(userId, userId);
    }

    // ChatRoom ID로 조회
    public Optional<ChatRoom> findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

}