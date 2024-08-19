package com.lec.spring.repository;

import com.lec.spring.domain.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUser_userId(Long userId, Sort sort);
    List<Product> findByCategory2(String category2);
}