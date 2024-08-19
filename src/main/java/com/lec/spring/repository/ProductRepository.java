package com.lec.spring.repository;

import com.lec.spring.domain.Product;
import com.lec.spring.domain.enums.DealingStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUser_userId(Long userId, Sort sort);
    List<Product> findByCategory2(String category2);

//    List<Product> findTop10ByOrderByViewCountDesc(DealingStatus dealingStatus);
    List<Product> findTop10ByDealingStatusOrderByViewCountDesc(DealingStatus dealingStatus);

    List<Product> findTop10ByDealingStatusOrderByRegDateDesc(DealingStatus dealingStatus);
}