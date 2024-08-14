package com.lec.spring.service;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Message;
import com.lec.spring.domain.User;
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
        chatRoom.setIsJoin(2L);     // 채팅방 생성 시 isJoin 컬럼 기본값 2로 설정
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

        //
        for (ChatRoom chatRoom : chatRooms) {
            Message lastMessage = messageRepository.findLastMessageByChatRoomId(chatRoom.getChatRoomId());
            chatRoom.setLastMessage(lastMessage != null ? lastMessage.getMessageText() : "대화 내용이 없습니다.");

            Message lastSendTime = messageRepository.findLastMessageByChatRoomId(chatRoom.getChatRoomId());
            chatRoom.setLastSendTime(lastSendTime != null ? lastSendTime.getSendTime() : null);

            Long unreadMessage = messageRepository.unreadMessage(chatRoom.getChatRoomId(), userId); // 수정된 부분
            chatRoom.setUnreadMessage(unreadMessage != null ? unreadMessage : 0L);
        }
        return chatRooms;
    }

    // 채팅방 나가기
    public void leaveChatRoom(Long chatRoomId, Long userId) {
        try {
            Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(chatRoomId);
            if (optionalChatRoom.isPresent()) {
                ChatRoom chatRoom = optionalChatRoom.get();

                if (chatRoom.getBuyerId() != null && chatRoom.getBuyerId().getUserId().equals(userId)) {
                    // 구매자가 채팅방 나갈 때
                    chatRoom.setBuyerId(null);
                } else if (chatRoom.getSellerId() != null && chatRoom.getSellerId().getUserId().equals(userId)) {
                    // 판매자가 채팅방을 나갈 때
                    chatRoom.setSellerId(null);
                }

                // 채팅방 참여 상태 업데이트
                updateChatRoomJoin(chatRoom);

                chatRoomRepository.save(chatRoom);
            } else {
                // 채팅방이 존재하지 않는 경우
                throw new RuntimeException("Chat room not found.");
            }
        } catch (Exception e) {
            // 예외 처리 및 로그
            e.printStackTrace();
            throw new RuntimeException("Error leaving chat room: " + e.getMessage());
        }
    }

    public Long leaveMessage(Long chatRoomId) {

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(chatRoomId);

        if (optionalChatRoom.isPresent()) {
            ChatRoom chatRoom = optionalChatRoom.get();
            updateChatRoomJoin(chatRoom); // 참여 상태 업데이트
            return chatRoom.getIsJoin(); // 참여 여부 반환
        } else {
            throw new RuntimeException("Chat room not found.");
        }
    }

    private void updateChatRoomJoin(ChatRoom chatRoom) {
        Long buyerId = chatRoom.getBuyerId() != null ? chatRoom.getBuyerId().getUserId() : null;
        Long sellerId = chatRoom.getSellerId() != null ? chatRoom.getSellerId().getUserId() : null;

        if (buyerId != null && sellerId != null) {
            chatRoom.setIsJoin(2L);     // 둘 다 참여
        } else if (buyerId != null || sellerId != null) {
            chatRoom.setIsJoin(1L);     // 한 명만 나감
        } else {
            chatRoom.setIsJoin(0L);     // 둘 다 나감
        }
    }
    // review 사용
    public ChatRoom findByChatRoomId(Long chatRoomId){
        return chatRoomRepository.findByChatRoomId(chatRoomId);
    }

}