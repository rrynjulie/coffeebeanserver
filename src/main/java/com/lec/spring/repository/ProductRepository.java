package com.lec.spring.repository;

import com.lec.spring.domain.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUser_userId(Long userId, Sort sort);
    @Query("SELECT p FROM product p WHERE "
            + "(:category1 IS NULL OR p.category1 = :category1) AND "
            + "(:category2 IS NULL OR p.category2 = :category2) AND "
            + "(:category3 IS NULL OR p.category3 = :category3)")
    List<Product> findProductsByCategories(
            @Param("category1") String category1,
            @Param("category2") String category2,
            @Param("category3") String category3
    );

//    List<Product> findByCategory1AndCategory2AndCategory3(String category1, String category2, String category3);
}