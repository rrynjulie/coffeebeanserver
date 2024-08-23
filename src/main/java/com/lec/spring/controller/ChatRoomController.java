package com.lec.spring.controller;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Product;
import com.lec.spring.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatRooms")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam Long productId,
                                                   @RequestParam Long buyerId) {
        ChatRoom createChatRoom = chatRoomService.createChatRoom(productId, buyerId);
        return ResponseEntity.ok(createChatRoom);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoom>> findByUserId(@PathVariable Long userId) {
        List<ChatRoom> chatRooms = chatRoomService.findByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }

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

    @GetMapping("/leave/{chatRoomId}/{userId}")
    public ResponseEntity<Long> leaveMessage(@PathVariable Long chatRoomId,
                                             @PathVariable Long userId) {
        Long isJoinStatus = chatRoomService.leaveMessage(chatRoomId);
        return ResponseEntity.ok(isJoinStatus);
    }

    // 채팅방 ID를 통해 상품 정보 조회
    @GetMapping("/chatRoom/{chatRoomId}/product")
    public ResponseEntity<Product> getProductByChatRoomId(@PathVariable Long chatRoomId) {
        Product product = chatRoomService.getProductByChatRoomId(chatRoomId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}