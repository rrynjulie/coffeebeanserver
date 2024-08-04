package com.lec.spring.controller;

import com.lec.spring.domain.Message;
import com.lec.spring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/sendMessage/{chatRoomId}")
    @SendTo("/topic/public/{chatRoomId}")
    public Message sendMessage(Message message) {
        message.setSendTime(LocalDateTime.now());
        message.setIsRead(false);
        return messageService.sendMessage(message);
    }

    @GetMapping("/api/room/{chatRoomId}")
    public List<Message> MessageByRoomId(@PathVariable Long chatRoomId) {
        return messageService.MessageByRoomId(chatRoomId);
    }

    @GetMapping("/api/user/{userId}")
    public List<Message> MessageByUserId(@PathVariable Long userId) {
        return messageService.MessageByUserId(userId);
    }

    @DeleteMapping("/api/{messageId}")
    public void deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
    }
}