package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.domain.enums.Position;
import com.lec.spring.domain.enums.Whether;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long propertyId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;  // 판매자

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Position position;  // 글 작성자 신분

    @Column(nullable = false)
    private String category;  // 부동산 종류(원룸, 빌라(투룸이상), 오피스텔, 아파트, 상가, 기타(사무실, 주택, 토지 등))

    @Column(columnDefinition = "LONGTEXT")
    private String propertyIntro;  // 매물 소개

    @Column(nullable = false)
    private String location;  // 주소

    @Column
    private String locationIntro;  // 위치 한 줄 설명

    @Column(nullable = false)
    private String name;  // 부동산 이름

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault(value = "'판매중'")
    @Column(nullable = false)
    private DealingStatus dealingStatus;  // 거래 상태

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime regDate;  // 등록 날짜

    @Column
    private Integer roomCount;  // 방 갯수(아파트, 오피스텔)

    @Column
    private Integer exclusiveArea;  // 전용 면적(원룸)

    @Column
    private Integer supplyArea;  // 공급 면적(원룸)

    @Column
    private Integer totalFloor;  // 총 층수(원룸)

    @Column
    private Integer floor;  // 부동산이 위치한 층수(원룸)

    @Column
    private Integer bathCount;  // 화장실 갯수(아파트, 오피스텔)

    @Column
    private Integer maintenanceCost;  // 관리비(아파트, 오피스텔)

    @Enumerated(value = EnumType.STRING)
    @Column
    private Whether loan;  // 대출 여부

    @Enumerated(value = EnumType.STRING)
    @Column
    private Whether pet;  // 반려동물 여부

    @Enumerated(value = EnumType.STRING)
    @Column
    private Whether parking;  // 주차 가능 여부

    @Column
    private Integer premium;  // 권리금

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate moveInDate;  // 입주 날짜

    @Column
    private String phoneNum;  // 전화번호

    @Column
    private String costIncluded;  // 관리비에 포함된 항목

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "dealingTypeId")
    @ToString.Exclude
    @Builder.Default   // builder 제공 안 함
    private List<DealingType> dealingTypes = new ArrayList<>();  // 거래 유형

    @PrePersist
    protected void onCreate() {  // 등록 날짜 초기화
        this.regDate = LocalDateTime.now();
    }

    public void addTypes(DealingType... types) {  // 거래 유형 추가
        Collections.addAll(dealingTypes, types);
    }
}