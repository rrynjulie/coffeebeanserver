package com.lec.spring.service;

import com.lec.spring.domain.Message;
import com.lec.spring.domain.User;
import com.lec.spring.repository.MessageRepository;
import com.lec.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Message sendMessage(Message message, Long senderId) {
        if (senderId != null) {
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            message.setSender(sender);
        } else {
            throw new RuntimeException("Sender ID is required but not provided");
        }

        // 한국 표준시(KST)로 현재 시간 설정
        ZonedDateTime koreaTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        message.setSendTime(koreaTime.toLocalDateTime());
        message.setIsRead(false);  // 처음에 메시지를 보낼 때 isRead는 false로 설정

        Message savedMessage = messageRepository.save(message);

        messagingTemplate.convertAndSend("/topic/public/" + message.getChatRoom().getChatRoomId(), savedMessage);

        return savedMessage;
    }

    @Transactional
    public void markMessagesAsRead(Long chatRoomId, Long userId) {
        List<Message> messages = messageRepository.findByChatRoomChatRoomId(chatRoomId);

        for (Message message : messages) {
            if (message.getSender() != null && message.getSender().getUserId() != null &&
                    !message.getSender().getUserId().equals(userId)) {
                // 메시지가 읽히지 않았고, 다른 사용자가 보낸 메시지일 때만 읽음으로 표시
                if (!message.getIsRead()) {
                    message.setIsRead(true);
                    messageRepository.save(message);
                }
            }
        }

        // Notify clients that the messages have been read
        messagingTemplate.convertAndSend("/topic/public/" + chatRoomId, "messagesRead");
    }

    public List<Message> MessageByRoomId(Long chatRoomId) {
        return messageRepository.findByChatRoomChatRoomId(chatRoomId);
    }

    public List<Message> findBySenderId(Long userId) {
        return messageRepository.findBySenderId(userId);
    }

    @Transactional
    public void deleteMessage(Long messageId, LocalDateTime sendTime) {
        ZonedDateTime koreaTimeNow = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime currentTime = koreaTimeNow.toLocalDateTime();
        long minutesBetween = java.time.Duration.between(sendTime, currentTime).toMinutes();
        if (minutesBetween <= 5) {
            messageRepository.deleteById(messageId);
        } else {
            throw new RuntimeException("Message can only be deleted within 5 minutes of creation.");
        }
    }
}
