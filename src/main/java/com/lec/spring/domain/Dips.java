package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "dips")
public class Dips {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long dipsId;  // 찜 ID
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;  // 판매자
    @Column(name = "productId")
    private Long productId;  // 상품 ID
    @Column(name = "carId")
    private Long carId;  // 중고차 ID
    @Column(name = "propertyId")
    private Long propertyId;  // 부동산 ID
}