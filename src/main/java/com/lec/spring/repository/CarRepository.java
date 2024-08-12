package com.lec.spring.repository;

import com.lec.spring.domain.Car;
import com.lec.spring.domain.enums.DealingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findCarByCategory2AndDealingStatus(String category2, DealingStatus dealingStatus);
}