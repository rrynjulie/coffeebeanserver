package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault(value = "'판매중'")
    @Column(nullable = false)
    private DealingStatus dealingStatus;  // 거래 상태

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime regDate;  // 등록 날짜

    @Column(nullable = false)
    private String category1;  // 제조국(국산차, 수입차)

    @Column(nullable = false)
    private String category2;  // 제조사(제네시스, 현대, 기아, 쉐보레/대우,  르노코리아, 벤츠, BMW, 아우디, 테슬라, 포르쉐)


    @Enumerated(value = EnumType.STRING)
    @ColumnDefault(value = "'중고'")
    @Column(nullable = false)
    private Status status;  // 중고차 여부

    @Column
    private Integer modelYear;  // 연식(0000년)

    @Column
    private String carNum;  // 차량 번호

    @Column
    private Double distance;  // 주행 거리

    @Column
    private Integer displacement;  // 배기량

    @Column
    private String fuel;  // 연료(가솔린, 디젤, LPG, CNG, 전기, 하이브리드)

    @Column
    private String transmission;  // 변속기(자동, 수동, CVT, 듀얼클러치)

    @Column
    private Integer insuranceVictim;  // 보험사고(피해) 이력 횟수

    @Column
    private Integer insuranceInjurer;  // 보험사고(가해) 이력 횟수

    @Column
    private Integer ownerChange;  // 소유자 변경 이력 횟수

    @ColumnDefault(value = "0")
    @Column(nullable = false, insertable = false)
    private int viewCount;  // 조회수

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "carId")
    @ToString.Exclude
    @Builder.Default   // builder 제공 안 함
    private List<Attachment> fileList = new ArrayList<>();  // 첨부 파일

    @PrePersist
    protected void onCreate() {  // 등록 날짜 초기화
        this.regDate = LocalDateTime.now();
    }

    public void addFiles(Attachment... files) {  // 첨부파일 추가
        Collections.addAll(fileList, files);
    }
}