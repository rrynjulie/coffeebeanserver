package com.lec.spring.controller;

import com.lec.spring.domain.Message;
import com.lec.spring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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

//    @GetMapping

}