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
@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;  // 상품 ID

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;  // 판매자

    @Column(nullable = false)
    private String name;  // 상품 이름

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String description;  // 상품 설명

    @Column(nullable = false)
    private int price;  // 상품 가격

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime regDate;  // 등록 날짜

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault(value = "'판매중'")
    @Column(nullable = false)
    private DealingStatus dealingStatus;  // 판매 상태

    @Column(nullable = false)
    private String category1;  // 카테고리1(패션의류, 모바일/태블릿, 가구/인테리어, 반려동물/취미, 티켓/쿠폰)

    @Column(nullable = false)
    private String category2;  // 카테고리2(여성의류, 남성의류, 스마트폰, 태블릿PC, 케이스/거치대/보호필름, 배터리/충전기/케이블, 침실가구, 거실가구, 주방가구, 기타가구, 반려동물, 키덜트, 핸드메이드/DIY, 악기, 티켓, 상품권/쿠폰)

    @Column
    private String category3;  // 카테고리3

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault(value = "'중고'")ㄴ
    @Column(nullable = false)
    private Status status;  // 상품 상태

    @Column(nullable = false)
    private String dealingType;  // 거래 방식 (직거래, 택배)

    @Column
    private String desiredArea;  // 거래 희망 지역

    @ColumnDefault(value = "0")
    @Column(nullable = false, insertable = false)
    private int viewCount;  // 조회수

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachmentId")
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