package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    private Long isJoin; // 채팅방 참여 여부

    @ManyToOne
    @JoinColumn(name = "buyerId", referencedColumnName = "userId")
    @JsonIgnore
    private User buyerId; // User 객체 참조

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "sellerId", referencedColumnName = "userId")
    @JsonIgnore
    private User sellerId; // 판매자 User 객체 참조

    @Transient
    private String lastMessage; // 마지막 메시지

    @Transient
    @JsonFormat(pattern = "MM월 dd일 a HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime lastSendTime; // 마지막 메시지 보낸 시간

    @Transient
    private Long unreadMessage; // 안 읽은 메시지 개수

    @Transient
    private List<Attachment> attachments; // 첨부파일 리스트 추가

    private boolean dealComplete;

    @OneToMany(mappedBy = "chatRoom")
    @JsonIgnore
    private List<Message> messages;

    public boolean getDealComplete() {
        return false;
    }
}
