package com.lec.spring.service;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    // 기본적인 CRUD
    @Transactional
    public ChatRoom create(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Transactional(readOnly = true)
    public ChatRoom readOne(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> readAll() {
        return chatRoomRepository.findAll();
    }

    @Transactional
    public ChatRoom update(ChatRoom chatRoom) {
        ChatRoom chatRoomEntity = chatRoomRepository.findById(chatRoom.getChatRoomId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return chatRoomEntity;
    }

    @Transactional
    public String delete(Long chatRoomId) {
        chatRoomRepository.deleteById(chatRoomId);
        return "ok";
    }

    // 추가 기능
    // TODO
}