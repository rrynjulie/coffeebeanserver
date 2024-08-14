package com.lec.spring.controller;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Message;
import com.lec.spring.service.ChatRoomService;
import com.lec.spring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatRoomService chatRoomService;

    @MessageMapping("/sendMessage/{chatRoomId}")
    @SendTo("/topic/public/{chatRoomId}")
    public Message sendMessage(Message message, @DestinationVariable Long chatRoomId) {     // @DestinationVariable ---> url 경로에서 추출한 chatRoomId
        System.out.println("Received message: " + message);  // 로그 추가

        // ChatRoom 조회
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId)
                .orElseThrow();

        message.setSendTime(LocalDateTime.now());   // 메세지 전송 시간
        message.setIsRead(false);                   // 상대방 메세지 확인 여부
        message.setChatRoom(chatRoom);              // 메세지가 어느 채팅방에 속하는지 ?

        return messageService.sendMessage(message);
    }

    @PostMapping("/messages/read/{chatRoomId}/{userId}")
    public void markMessagesAsRead(@PathVariable Long chatRoomId, @PathVariable Long userId) {
        messageService.markMessagesAsRead(chatRoomId, userId);
    }

    @GetMapping("/messages/{chatRoomId}")
    public List<Message> getMessagesByChatRoomId(@PathVariable Long chatRoomId) {
        return messageService.MessageByRoomId(chatRoomId);
    }

    @GetMapping("/user/{userId}")
    public List<Message> MessageByUserId(@PathVariable Long userId) {
        return messageService.MessageByUserId(userId);
    }

    @DeleteMapping("/messages/{messageId}")
    public void deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
    }

    @GetMapping("/leave/{chatRoomId}")
    public ResponseEntity<Long> leaveMessage(@PathVariable Long chatRoomId) {
        Long isJoin = chatRoomService.leaveMessage(chatRoomId);
        return ResponseEntity.ok(isJoin);
    }

}