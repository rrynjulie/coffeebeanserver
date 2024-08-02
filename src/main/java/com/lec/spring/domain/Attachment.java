package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long attachmentId;

    @ManyToOne
    private Post postId;
    @ManyToOne
    private Product productId;
    @ManyToOne
    private Car carId;
    @ManyToOne
    private Property propertyId;

    private String source;
    private String filename;

    @Transient
    private boolean isImage;
}