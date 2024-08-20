package com.lec.spring.service;

import com.lec.spring.domain.*;
import com.lec.spring.repository.ChatRoomRepository;
import com.lec.spring.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // 새로운 채팅방 생성 및 데이터베이스 저장
    @Transactional
    public ChatRoom createChatRoom(Long productId, Long buyerUserId) {
        Product product = productService.readOne(productId);
        User buyer = userService.readOne(buyerUserId);
        User seller = product.getUser();

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setProduct(product);
        chatRoom.setBuyerId(buyer); // 올바르게 설정됨
        chatRoom.setSellerId(seller); // 올바르게 설정됨
        chatRoom.setIsJoin(2L);  // isJoin 을 2로 설정
        chatRoom.setDealComplete(false); // 거래 완료 여부 초기값 설정

        return chatRoomRepository.save(chatRoom);
    }

    // 사용자가 참여하고 있는 모든 채팅방 조회
    public List<ChatRoom> findByUserId(Long userId) {
        return chatRoomRepository.findByUserId(userId);
    }

    // ChatRoom ID로 조회
    public Optional<ChatRoom> findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

    // 채팅방 목록과 각 채팅방의 마지막 메시지를 가져오는 메소드
    @Transactional
    public List<ChatRoom> findChatRoomsWithLastMessage(Long userId) {
        List<ChatRoom> chatRooms = findByUserId(userId);

        for (ChatRoom chatRoom : chatRooms) {
            List<Message> lastMessages = messageRepository.findLastMessageByChatRoomId(chatRoom.getChatRoomId(), PageRequest.of(0, 1));
            if (!lastMessages.isEmpty()) {
                Message lastMessage = lastMessages.get(0);
                chatRoom.setLastMessage(lastMessage.getMessageText());
                chatRoom.setLastSendTime(lastMessage.getSendTime());
            } else {
                chatRoom.setLastMessage("대화 내용이 없습니다.");
                chatRoom.setLastSendTime(null);
            }

            Long unreadMessage = messageRepository.unreadMessage(chatRoom.getChatRoomId(), userId);
            chatRoom.setUnreadMessage(unreadMessage != null ? unreadMessage : 0L);

            Product product = chatRoom.getProduct();
            if (product != null) {
                List<Attachment> attachments = product.getFileList();
                chatRoom.setAttachments(attachments); // ChatRoom 에 첨부파일 리스트 추가
            }
        }
        return chatRooms;
    }

    // 채팅방 목록에서 각 채팅방의 상품 정보만 가져오는 메소드
    @Transactional
    public Product getProductByChatRoomId(Long chatRoomId) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(chatRoomId);
        if (chatRoomOpt.isPresent()) {
            ChatRoom chatRoom = chatRoomOpt.get();
            return chatRoom.getProduct();
        }
        return null; // 또는 적절한 예외를 던질 수 있습니다.
    }

    // 채팅방 나가기
    public void leaveChatRoom(Long chatRoomId, Long userId) {
        try {
            Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(chatRoomId);
            if (optionalChatRoom.isPresent()) {
                ChatRoom chatRoom = optionalChatRoom.get();

                if (chatRoom.getSellerId() != null && chatRoom.getSellerId().getUserId().equals(userId)) {
                    chatRoom.setSellerId(null);
                } else if (chatRoom.getBuyerId() != null && chatRoom.getBuyerId().getUserId().equals(userId)) {
                    chatRoom.setBuyerId(null);
                }

                updateChatRoomJoin(chatRoom);
                chatRoomRepository.save(chatRoom);
            } else {
                throw new RuntimeException("Chat room not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error leaving chat room: " + e.getMessage());
        }
    }

    // 채팅방 참여 상태 업데이트
    private void updateChatRoomJoin(ChatRoom chatRoom) {
        Long buyerId = chatRoom.getBuyerId() != null ? chatRoom.getBuyerId().getUserId() : null;
        Long sellerId = chatRoom.getSellerId() != null ? chatRoom.getSellerId().getUserId() : null;

        if (buyerId != null && sellerId != null) {
            chatRoom.setIsJoin(2L); // 둘 다 참여
        } else if (buyerId != null || sellerId != null) {
            chatRoom.setIsJoin(1L); // 한 명만 나감
        } else {
            chatRoom.setIsJoin(0L); // 둘 다 나감
        }
    }

    // 채팅방 ID로 조회
    public Optional<ChatRoom> findByChatRoomId(Long chatRoomId) {
        return chatRoomRepository.findByChatRoomId(chatRoomId);
    }

    // 채팅방 나가면서 참여 여부 조회
    public Long leaveMessage(Long chatRoomId) {
        Optional<ChatRoom> optionalChatRoom = findByChatRoomId(chatRoomId);

        if (optionalChatRoom.isPresent()) {
            ChatRoom chatRoom = optionalChatRoom.get();
            updateChatRoomJoin(chatRoom); // 참여 상태 업데이트
            chatRoomRepository.save(chatRoom);  // 저장
            return chatRoom.getIsJoin(); // 참여 여부 반환
        } else {
            throw new RuntimeException("Chat room not found.");
        }
    }
}