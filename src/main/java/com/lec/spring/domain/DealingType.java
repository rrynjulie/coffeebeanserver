package com.lec.spring.domain;

import com.lec.spring.domain.enums.DealType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "dealing_type")
public class DealingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dealingTypeId;  // 거래 방식 ID

    @ManyToOne(optional = false)
    @JoinColumn(name = "propertyId", nullable = false)
    @ToString.Exclude
    private Property property;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private DealType dealType;  // 거래 방식

    @Column
    private Integer deposit;  // 계약금(보증금)

    @Column
    private Integer rent;  // 세(월세, 전세)
}