package com.lec.spring.controller;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Message;
import com.lec.spring.domain.User;
import com.lec.spring.service.ChatRoomService;
import com.lec.spring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    @Transactional
    public Message sendMessage(@DestinationVariable Long chatRoomId, @Payload Message message) {
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        if (message.getSender() == null || message.getSender().getUserId() == null) {
            throw new RuntimeException("Sender information is missing");
        }

        // Set the time in Korea timezone
        ZonedDateTime koreaTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        message.setSendTime(koreaTime.toLocalDateTime());
        message.setIsRead(false);
        message.setChatRoom(chatRoom);

        Message savedMessage = messageService.sendMessage(message, message.getSender().getUserId());

        // 메시지 읽음 상태 업데이트
        markMessagesAsRead(chatRoomId, message.getSender().getUserId());

        // 읽음 상태 업데이트를 위한 메시지 전송
        return savedMessage;
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
    public List<Message> findBySenderId(@PathVariable Long userId) {
        return messageService.findBySenderId(userId);
    }


    @GetMapping("/leave/{chatRoomId}")
    public ResponseEntity<Long> leaveMessage(@PathVariable Long chatRoomId) {
        Long isJoin = chatRoomService.leaveMessage(chatRoomId);
        return ResponseEntity.ok(isJoin);
    }
    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long messageId, @RequestParam String sendTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime parsedSendTime = LocalDateTime.parse(sendTime, formatter);

            messageService.deleteMessage(messageId, parsedSendTime);

            return ResponseEntity.ok("Message deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting message: " + e.getMessage());
        }
    }
}
