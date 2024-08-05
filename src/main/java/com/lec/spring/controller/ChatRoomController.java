package com.lec.spring.controller;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("chatRooms")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    // 채팅방 생성 및 저장
    @PostMapping
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoom chatRoom) {
        ChatRoom createChatRoom = chatRoomService.createChatRoom(chatRoom);
        return ResponseEntity.ok(createChatRoom);
    }

    // 사용자가 참여하고 있는 모든 채팅방
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoom>> findByUserId(@PathVariable Long userId) {
        List<ChatRoom> chatRooms = chatRoomService.findByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }


}