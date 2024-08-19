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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
        DealingStatus tempDS = DealingStatus.valueOf(dealingStatus);

        if(entityType.equals("product")) {  // 중고 물품일때
            dipsList.forEach(dips -> {
                if(dips.getProduct() != null) productList.add(dips.getProduct());
            });
            if(sortType == 1) productList.sort(Comparator.comparing(Product::getRegDate).reversed());  // 등록일시 내림차순
            else if(sortType == 2)productList.sort(Comparator.comparing(Product::getPrice));  // 가격 오름차순
            else productList.sort(Comparator.comparing(Product::getPrice).reversed());  // 가격 내림차순
            return productList
                    .stream()
                    .filter(product -> product.getDealingStatus().equals(tempDS))
                    .collect(Collectors.toList());
        } else if(entityType.equals("car")) {  // 중고차일때
            dipsList.forEach(dips -> {
                if(dips.getCar() != null) carList.add(dips.getCar());
            });
            if(sortType == 1) carList.sort(Comparator.comparing(Car::getRegDate).reversed());  // 등록일시 내림차순
            else if(sortType == 2)carList.sort(Comparator.comparing(Car::getPrice));  // 가격 오름차순
            else carList.sort(Comparator.comparing(Car::getPrice).reversed());  // 가격 내림차순
            return carList
                    .stream()
                    .filter(car -> car.getDealingStatus().equals(tempDS))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid entityType: " + entityType);
        }
    }

    @Transactional
    public String delete(Long dipsId) {
        dipsRepository.deleteById(dipsId);
        return "ok";
    }
}