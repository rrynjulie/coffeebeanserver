package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@Entity(name = "attachment")
@DynamicInsert
@DynamicUpdate
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "postId")
    @ToString.Exclude
    private Post post;

    @ManyToOne(optional = true)
    @JoinColumn(name = "productId")
    @ToString.Exclude
    private Product product;

    @ManyToOne(optional = true)
    @JoinColumn(name = "carId")
    @ToString.Exclude
    private Car car;

    private String source;
    private String filename;

    @Transient
    private boolean isImage;
}