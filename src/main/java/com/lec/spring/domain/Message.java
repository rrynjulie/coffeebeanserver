package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;             // 메세지 id

    private String messageText;         // 메세지 내용

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sendTime;     // 메세지 보낸 시간

    @Column(nullable = false)
    private boolean IsRead;             // 메세지 읽음 여부

    @Transient
    private boolean lastIsRead;         // 마지막 메세지 읽음 여부

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;        // ChatRoom 테이블과의 관계

    @ManyToOne
    @JoinColumn(name = "senderId", referencedColumnName = "userId")
    private User sender;               // 메세지를 보낸 User
}