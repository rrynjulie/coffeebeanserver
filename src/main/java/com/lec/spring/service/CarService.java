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
    private final UserService userService;

    // 기본적인 CRUD
    @Transactional
    public Car create(Car car, Long userId) {
        car.setUser(userService.readOne(userId));
        return carRepository.save(car);
    }

    @Transactional
    public Car readOne(Long carId) {
        Car carEntity = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        carEntity.setViewCount(carEntity.getViewCount() + 1);
        return carRepository.save(carEntity);
    }

    @Transactional(readOnly = true)
    public List<Car> readAll() {
        return carRepository.findAll();
    }

    @Transactional
    public Car update(Car car) {
        Car carEntity = carRepository.findById(car.getCarId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        carEntity.setName(car.getName());
        carEntity.setPrice(car.getPrice());
        carEntity.setIntroduce(car.getIntroduce());
        carEntity.setCategory1(car.getCategory1());
        carEntity.setCategory2(car.getCategory2());
        carEntity.setStatus(car.getStatus());
        carEntity.setModelYear(car.getModelYear());
        carEntity.setCarRegDate(car.getCarRegDate());
        carEntity.setDistance(car.getDistance());
        carEntity.setDisplacement(car.getDisplacement());
        carEntity.setFuel(car.getFuel());
        carEntity.setTransmission(car.getTransmission());
        carEntity.setInsuranceVictim(car.getInsuranceVictim());
        carEntity.setInsuranceInjurer(car.getInsuranceInjurer());
        carEntity.setSpecialUse(car.getSpecialUse());
        carEntity.setOwnerChange(car.getOwnerChange());
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