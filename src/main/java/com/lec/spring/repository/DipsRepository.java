package com.lec.spring.repository;

import com.lec.spring.domain.Dips;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DipsRepository extends JpaRepository<Dips, Long> {
    List<Dips> findByUser_userId(Long userId);

    //찜 상태(product)
    boolean existsByUser_UserIdAndProduct_ProductId(Long userId, Long productId);

    //찜 상태(car)
    boolean existsByUser_UserIdAndCar_CarId(Long userId, Long carId);

    Dips findByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
    Dips findByUser_UserIdAndCar_CarId(Long userId, Long carId);

    //찜 개수
    int countByProduct_ProductId(Long productId);

    int countByCar_CarId(Long carId);


}