package com.lec.spring.service;

import com.lec.spring.domain.Car;
import com.lec.spring.domain.Dips;
import com.lec.spring.domain.Product;
import com.lec.spring.repository.DipsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DipsService {
    private final DipsRepository dipsRepository;
    private final UserService userService;

    // 기본적인 CRUD
    @Transactional
    public Dips create(Dips dips, Long userId) {
        dips.setUser(userService.readOne(userId));
        return dipsRepository.save(dips);
    }

    @Transactional(readOnly = true)
    public List<Dips> readAll() {
        return dipsRepository.findAll();
    }

    @Transactional
    public String delete(Long dipsId) {
        dipsRepository.deleteById(dipsId);
        return "ok";
    }

    // 추가 기능
//    @Transactional(readOnly = true)
//    public List<?> readByUserId(Long userId, String entityType, int sortType) {
//        Sort sort;
//        if(sortType == 1) sort = Sort.by(Sort.Order.desc("regDate"));
//        else if(sortType == 2) sort = Sort.by(Sort.Order.asc("price"));
//        else sort = Sort.by(Sort.Order.desc("price"));
//        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
//        if(entityType.equals("product")) {
//            return dipsRepository.findByUser_userId(userId, pageable).stream()
//                    .map(Dips::getProduct)
//                    .collect(Collectors.toList());
//        } else if (entityType.equals("car")) {
//            return dipsRepository.findByUser_userId(userId, pageable).stream()
//                    .map(Dips::getCar)
//                    .collect(Collectors.toList());
//        } else {
//            throw new IllegalArgumentException("Invalid entityType: " + entityType);
//        }
//    }
}