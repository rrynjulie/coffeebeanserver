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

    public Message sendMessage(Message message) {
        message.setSendTime(LocalDateTime.now());
        message.setIsRead(false);
        return messageRepository.save(message);
    }

    public List<Message> MessageByRoomId(Long chatRoomId) {
        return messageRepository.findByChatRoomId(chatRoomId);
    }

    public List<Message> MessageByUserId(Long userId) {
        return messageRepository.findByUserId(userId);
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

}