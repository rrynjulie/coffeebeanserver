package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "productId")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Column(nullable = false)
    private int price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime regDate;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault(value = "'판매중'")
    @Column(nullable = false)
    private DealingStatus dealingStatus;

    @Column(nullable = false)
    private String category1;

    @Column(nullable = false)
    private String category2;

    @Column
    private String category3;

    @Enumerated(value = EnumType.STRING)
    @ColumnDefault(value = "'중고'")
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String dealingType;

    @Column
    private String desiredArea;

    @ColumnDefault(value = "0")
    @Column(nullable = false, insertable = false)
    private int viewCount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @ToString.Exclude
    @Builder.Default
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @ToString.Exclude
    @Builder.Default
    private List<Attachment> fileList = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.regDate = LocalDateTime.now();
    }

    public void addFiles(Attachment... files) {
        Collections.addAll(fileList, files);
    }
}