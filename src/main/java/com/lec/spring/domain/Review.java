package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long reviewId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chatroomId", nullable = false)
    @ToString.Exclude
    private ChatRoom chatRoom;

    @ManyToOne(optional = true)
    @JoinColumn(name = "writerId", nullable = true)
    @ToString.Exclude
    private User writer;

    @ManyToOne(optional = true)
    @JoinColumn(name = "recipientId", nullable = true)
    @ToString.Exclude
    private User recipient;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime regDate;
}