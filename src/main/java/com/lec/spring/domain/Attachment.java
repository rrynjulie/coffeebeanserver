package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;  // 첨부물 ID
    @Column(name = "postId")
    private Long postId;  // 게시글 ID
    @Column(name = "productId")
    private Long productId;  // 상품 ID
    @Column(name = "carId")
    private Long carId;  // 중고차 ID
    @Column(name = "propertyId")
    private Long propertyId;  // 부동산 ID
    @Column(nullable = false)
    private String source;  // 소스
    @Column(nullable = false)
    private String filename;  // 파일 이름
}