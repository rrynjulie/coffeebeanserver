package com.lec.spring.service;

import com.lec.spring.domain.Message;
import com.lec.spring.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;

    // 기본적인 CRUD
    @Transactional
    public Message create(Message message) {
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public Message readOne(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Transactional
    public Message update(Message message) {
        Message messageEntity = messageRepository.findById(message.getMessageId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return messageEntity;
    }

    @Transactional
    public String delete(Long messageId) {
        messageRepository.deleteById(messageId);
        return "ok";
    }

    // 추가 기능
    // TODO
}