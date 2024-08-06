package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "quit")
public class Quit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long quitId;  // 탈퇴 사유 ID

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;  // 회원

    @Column(nullable = false)
    private int badManners;  // 비매너 사용자(1:Yes, 2:No)

    @Column(nullable = false)
    private int lowFrequency;  // 이용 빈도 감소(1:Yes, 2:No)

    @Column(nullable = false)
    private int badService;  // 서비스 기능 불편(1:Yes, 2:No)

    @Column(nullable = false)
    private int eventUse ;  // 이벤트 등의 목적(1:Yes, 2:No)

    @Column(columnDefinition = "LONGTEXT")
    private String etc;  // 기타 사유
}