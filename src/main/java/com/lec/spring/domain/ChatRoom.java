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

    private Long isJoin;

    @ManyToOne
    @JoinColumn(name = "buyerId", referencedColumnName = "userId")
    @JsonIgnore
    private User buyerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "sellerId", referencedColumnName = "userId")
    @JsonIgnore
    private User sellerId;

    @Transient
    private String lastMessage;

    @Transient
    @JsonFormat(pattern = "MM월 dd일 a HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime lastSendTime;

    @Transient
    private Long unreadMessage;

    @Transient
    private List<Attachment> attachments;

    private boolean dealComplete;

    @OneToMany(mappedBy = "chatRoom")
    @JsonIgnore
    private List<Message> messages;

    public boolean getDealComplete() {
        return true;
    }
}
