package com.lec.spring.service;

import com.lec.spring.domain.Car;
import com.lec.spring.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CarService {
    private final CarRepository carRepository;

    // 기본적인 CRUD
    @Transactional
    public Car create(Car car) {
        return carRepository.save(car);
    }

    @Transactional(readOnly = true)
    public Car readOne(Long carId) {
        return carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Car> readAll() {
        return carRepository.findAll();
    }

    @Transactional
    public Car update(Car car) {
        Car carEntity = carRepository.findById(car.getCarId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return carEntity;
    }

    @Transactional
    public String delete(Long carId) {
        carRepository.deleteById(carId);
        return "ok";
    }

    // 추가 기능
    // TODO
}