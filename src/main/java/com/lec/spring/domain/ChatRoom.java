package com.lec.spring.domain;

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

    Long isJoins;    // 채팅방 참여 여부

    @ManyToOne
    @JoinColumn(name = "buyerId", referencedColumnName = "userId")
    private User user; // User 객체 참조

    @ManyToOne
    @JoinColumn(name = "sellerId", referencedColumnName = "userId", nullable = true)
    private Product product;    // 중고상품

//    @ManyToOne
//    @JoinColumn(name = "sellerId", referencedColumnName = "userId", nullable = true)
//    private Car car;          // 중고차
}