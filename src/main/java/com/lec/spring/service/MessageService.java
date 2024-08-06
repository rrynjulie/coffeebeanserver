package com.lec.spring.service;

import com.lec.spring.domain.Message;
import com.lec.spring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void markMessagesAsRead(Long chatRoomId, Long userId) {
        List<Message> messages = messageRepository.findByChatRoomChatRoomId(chatRoomId);
        for (Message message : messages) {
            if (!message.getSender().getUserId().equals(userId)) { // 상대방의 메시지만 업데이트
                message.setIsRead(true);
                messageRepository.save(message); // 업데이트된 메시지 저장
            }
        }
    }
    public Message sendMessage(Message message) {
        message.setSendTime(LocalDateTime.now());
        message.setIsRead(false);
        return messageRepository.save(message);
    }

    public List<Message> MessageByRoomId(Long chatRoomId) {
        return messageRepository.findByChatRoomChatRoomId(chatRoomId);
    }

    public List<Message> MessageByUserId(Long userId) {
        return messageRepository.findBySenderUserId(userId);
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

}