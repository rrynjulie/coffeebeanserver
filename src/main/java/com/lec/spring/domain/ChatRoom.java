package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long chatRoomId;

    Long isJoin;    // 채팅방 참여 여부

    @ManyToOne
    @JoinColumn(name = "buyerId", referencedColumnName = "userId")
    private User buyerId; // User 객체 참조

    @ManyToOne
    @JoinColumn(name = "sellerId", referencedColumnName = "userId", nullable = true)
    private User sellerId; // User 객체 참조

    @Transient      // DB 컬럼 아님
    private String lastMessage;     // 마지막 메세지

    @Transient
    @JsonFormat(pattern = "MM월 dd일 a HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime lastSendTime;     // 마지막 메세지 보낸 시간

    @Transient
    private Long unreadMessage;       // 안읽은 메세지 개수
}