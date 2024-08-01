package com.lec.spring.repository;

import com.lec.spring.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}