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
    @JoinColumn(name = "buyerId", nullable = true)
    @ToString.Exclude
    private User buyerId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "sellerId", nullable = true)
    @ToString.Exclude
    private User sellerId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime regDate;

    private Boolean isBuyerReview;

}