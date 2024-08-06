package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private boolean dealComplete;

    public boolean getDealComplete() {
        return true;
    }
}
