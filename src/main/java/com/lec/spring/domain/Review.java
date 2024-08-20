package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    @OneToOne(mappedBy = "review")
    @ToString.Exclude
    private SampleReview sampleReview;
}