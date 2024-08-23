package com.lec.spring.service;

import com.lec.spring.domain.Car;
import com.lec.spring.domain.Dips;
import com.lec.spring.domain.Product;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.repository.DipsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DipsService {
    private final DipsRepository dipsRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CarService carService;

    // 기본적인 CRUD
    @Transactional
    public long create(Dips dips, String entityType, Long userId, Long entityId) {
        dips.setUser(userService.readOne(userId));
        if(entityType.equals("product")) dips.setProduct(productService.readOne(entityId));
        else dips.setCar(carService.readOne(entityId));
        dips = dipsRepository.saveAndFlush(dips);
        return dips.getDipsId();
    }

    @Transactional(readOnly = true)
    public List<?> readByUserId(Long userId, String entityType, int sortType, String dealingStatus) {
        List<Dips> dipsList = dipsRepository.findByUser_userId(userId);
        List<Product> productList = new ArrayList<>();
        List<Car> carList = new ArrayList<>();

        if(entityType.equals("product")) {  // 중고 물품일때
            dipsList.forEach(dips -> {
                if(dips.getProduct() != null) productList.add(dips.getProduct());
            });
            if(sortType == 1) productList.sort(Comparator.comparing(Product::getRegDate).reversed());  // 등록일시 내림차순
            else if(sortType == 2) productList.sort(Comparator.comparing(Product::getPrice));  // 가격 오름차순
            else productList.sort(Comparator.comparing(Product::getPrice).reversed());  // 가격 내림차순

            if(dealingStatus.equals("전체")) return productList;
            DealingStatus tempDS = DealingStatus.valueOf(dealingStatus);

            return productList
                    .stream()
                    .filter(product -> product.getDealingStatus().equals(tempDS))
                    .collect(Collectors.toList());
        } else if(entityType.equals("car")) {  // 중고차일때
            dipsList.forEach(dips -> {
                if(dips.getCar() != null) carList.add(dips.getCar());
            });
            if(sortType == 1) carList.sort(Comparator.comparing(Car::getRegDate).reversed());  // 등록일시 내림차순
            else if(sortType == 2) carList.sort(Comparator.comparing(Car::getPrice));  // 가격 오름차순
            else carList.sort(Comparator.comparing(Car::getPrice).reversed());  // 가격 내림차순

            if(dealingStatus.equals("전체")) return carList;
            DealingStatus tempDS = DealingStatus.valueOf(dealingStatus);

            return carList
                    .stream()
                    .filter(car -> car.getDealingStatus().equals(tempDS))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid entityType: " + entityType);
        }
    }

//    @Transactional(readOnly = true)
//    public boolean isProductDipped(Long userId, Long productId) {
//        return dipsRepository.existsByUser_UserIdAndProduct_ProductId(userId, productId);
//    }

    //찜 상태(product)
    @Transactional(readOnly = true)
    public boolean isProductDippedByUser(Long userId, Long productId) {
        return dipsRepository.existsByUser_UserIdAndProduct_ProductId(userId, productId);
    }

    //찜 상태(car)
    @Transactional(readOnly = true)
    public boolean isCarDippedByUser(Long userId, Long carId) {
        return dipsRepository.existsByUser_UserIdAndCar_CarId(userId, carId);
    }

    @Transactional
    public boolean delete(Long userId, Long entityId, String entityType) {
        try {
            if (entityType.equals("product")) {
                // 중고 물품인 경우
                Dips dips = dipsRepository.findByUser_UserIdAndProduct_ProductId(userId, entityId);
                if (dips != null) {
                    dipsRepository.delete(dips);
                    return true;
                }
            } else if (entityType.equals("car")) {
                // 중고차인 경우
                Dips dips = dipsRepository.findByUser_UserIdAndCar_CarId(userId, entityId);
                if (dips != null) {
                    dipsRepository.delete(dips);
                    return true;
                }
            } else {
                throw new IllegalArgumentException("Invalid entityType: " + entityType);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}