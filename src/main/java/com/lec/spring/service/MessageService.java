package com.lec.spring.service;

import com.lec.spring.domain.Message;
import com.lec.spring.domain.User;
import com.lec.spring.repository.MessageRepository;
import com.lec.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    public void markMessagesAsRead(Long chatRoomId, Long userId) {
        List<Message> messages = messageRepository.findByChatRoomChatRoomId(chatRoomId);
        for (Message message : messages) {
            if (message.getSender() != null && !message.getSender().getUserId().equals(userId)) { // 상대방의 메시지만 업데이트
                message.setIsRead(true);
                messageRepository.save(message); // 업데이트된 메시지 저장
            }
        }
    }

    @Transactional
    public Message sendMessage(Message message, Long senderId) {
        if (senderId != null) {
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            message.setSender(sender); // sender 설정
        } else {
            throw new RuntimeException("Sender ID is required but not provided");
        }
        message.setSendTime(LocalDateTime.now());
        message.setIsRead(false);

        System.out.println("메세지: " + message);
        return messageRepository.save(message);
    }

    public List<Message> MessageByRoomId(Long chatRoomId) {
        return messageRepository.findByChatRoomChatRoomId(chatRoomId);
    }

    public List<Message> findBySenderId(Long userId) {
        return messageRepository.findBySenderId(userId);
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}
