package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "attachment")
@DynamicInsert
@DynamicUpdate
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "postId", nullable = true)
    @ToString.Exclude
    @JsonBackReference
    private Post post;

    @ManyToOne(optional = true)
    @JoinColumn(name = "productId", nullable = true)
    @ToString.Exclude
    @JsonBackReference
    private Product product;

    @ManyToOne(optional = true)
    @JoinColumn(name = "carId", nullable = true)
    @ToString.Exclude
    @JsonBackReference
    private Car car;

    private String source;
    private String filename;

    @Transient
    private boolean isImage;
}