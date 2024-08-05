package com.lec.spring.domain;

import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long carId;  // 중고차 ID

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;  // 판매자

    @Column(nullable = false)
    private String name;  // 중고차 이름

    @Column(nullable = false)
    private int price;  // 중고차 가격

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String introduce;  // 중고차 소개

    private DealingStatus dealingStatus;  // 거래 상태

    private LocalDateTime regDate;  // 등록 날짜

    private String option;

    private String category;  // 브랜드

    private Status status;  // 중고차 여부

    private Integer modelYear;  //
}