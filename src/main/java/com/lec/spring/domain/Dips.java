package com.lec.spring.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "dips")
public class Dips {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long dipsId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(optional = true)
    @JoinColumn(name = "propertyId", nullable = true)
    @ToString.Exclude
    private Property property;

    @ManyToOne(optional = true)
    @JoinColumn(name = "carId", nullable = true)
    @ToString.Exclude
    private Car car;

    @ManyToOne(optional = true)
    @JoinColumn(name = "productId", nullable = true)
    @ToString.Exclude
    private Product product;
}