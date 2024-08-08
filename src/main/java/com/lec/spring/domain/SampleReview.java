package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sample_review")
public class SampleReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long sampleReviewId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private User user;

    @OneToOne(optional = false)
    @JsonBackReference
    private Review review;

    @Column(nullable = false)
    private int manner;

    @Column(nullable = false)
    private int response;

    @Column(nullable = false)
    private int time;

    @Column(nullable = false)
    private int badManner;
}