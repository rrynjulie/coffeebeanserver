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
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam Long productId,
                                                   @RequestParam Long buyerId) {
        ChatRoom createChatRoom = chatRoomService.createChatRoom(productId, buyerId);
        return ResponseEntity.ok(createChatRoom);
    }


    // 사용자가 참여하고 있는 모든 채팅방
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoom>> findByUserId(@PathVariable Long userId) {
        List<ChatRoom> chatRooms = chatRoomService.findByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }

    // 사용자가 참여하고 있는 모든 채팅방과 마지막 메시지
    @GetMapping("/user/{userId}/with-last-message")
    public ResponseEntity<List<ChatRoom>> findChatRoomsWithLastMessage(@PathVariable Long userId) {
        List<ChatRoom> chatRooms = chatRoomService.findChatRoomsWithLastMessage(userId);
        return ResponseEntity.ok(chatRooms);
    }

    @PostMapping("/leave/{chatRoomId}/{userId}")
    public ResponseEntity<Void> leaveChatRoom(@PathVariable Long chatRoomId,
                                              @PathVariable Long userId) {
        chatRoomService.leaveChatRoom(chatRoomId, userId);
        return ResponseEntity.ok().build();
    }
}
