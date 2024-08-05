package com.lec.spring.service;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Message;
import com.lec.spring.repository.ChatRoomRepository;
import com.lec.spring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private MessageRepository messageRepository;

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

    // 채팅방 목록과 각 채팅방의 마지막 메시지를 가져오는 메소드
    public List<ChatRoom> findChatRoomsWithLastMessage(Long userId) {
        List<ChatRoom> chatRooms = findByUserId(userId);
        for (ChatRoom chatRoom : chatRooms) {
            Message lastMessage = messageRepository.findLastMessageByChatRoomId(chatRoom.getChatRoomId());
            chatRoom.setLastMessage(lastMessage != null ? lastMessage.getMessageText() : "대화 내용이 없습니다.");
        }
        for (ChatRoom chatRoom : chatRooms) {
            Message lastSendTime = messageRepository.findLastMessageByChatRoomId(chatRoom.getChatRoomId());
            chatRoom.setLastSendTime(lastSendTime != null ? lastSendTime.getSendTime() : LocalDateTime.parse("시간을 불러오지 못했습니다."));
        }
        return chatRooms;
    }
}