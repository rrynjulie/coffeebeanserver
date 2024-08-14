package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Car;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;
    private final AttachmentService attachmentService;

    // 기본적인 CRUD
    @Transactional
    public long create(Car car, Long userId, MultipartFile[] files) {
        car.setUser(userService.readOne(userId));
        car = carRepository.saveAndFlush(car);
        attachmentService.addFiles(files, car);
        return car.getCarId();
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
    public long update(Car car, Long carId, MultipartFile[] files, Long[] delfile) {
        Car carEntity = carRepository.findById(car.getCarId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));

        carEntity.setName(car.getName());
        carEntity.setPrice(car.getPrice());
        carEntity.setIntroduce(car.getIntroduce());
        carEntity.setCategory1(car.getCategory1());
        carEntity.setCategory2(car.getCategory2());
        carEntity.setStatus(car.getStatus());
        carEntity.setModelYear(car.getModelYear());
        carEntity.setCarNum(car.getCarNum());
        carEntity.setDistance(car.getDistance());
        carEntity.setDisplacement(car.getDisplacement());
        carEntity.setFuel(car.getFuel());
        carEntity.setTransmission(car.getTransmission());
        carEntity.setInsuranceVictim(car.getInsuranceVictim());
        carEntity.setInsuranceInjurer(car.getInsuranceInjurer());
        carEntity.setOwnerChange(car.getOwnerChange());

        carEntity = carRepository.saveAndFlush(carEntity);
        attachmentService.addFiles(files, carEntity);

        if(delfile != null) {
            for(Long fileId : delfile) {
                Attachment file = attachmentService.readOne(fileId);
                if(file != null) {
                    attachmentService.delFile(file);
                    attachmentService.delete(fileId);
                }
            }
        }

        return carId;
    }

    @Transactional
    public String delete(Long carId) {
        carRepository.deleteById(carId);
        return "ok";
    }

    @Transactional
    public List<Car> getRandomCarsByCategory2(String category2) {
        List<Car> cars = carRepository.findCarByCategory2AndDealingStatus(category2, DealingStatus.판매중);
        if (cars.size() <= 5) {
            return cars;
        }

        // 리스트를 무작위로 섞고 5개 선택
        Collections.shuffle(cars);
        return cars.stream().limit(5).collect(Collectors.toList());
    }

    @Transactional
    public List<Car> getFilteredCars(String category1, String category2) {
        if(category1 != null && category2 != null) {
            return carRepository.findCarByCategory1AndCategory2(category1, category2);
        } else if (category1 != null) {
            return carRepository.findCarByCategory1(category1);
        }else
            return carRepository.findAll();
    }

    // 추가 기능
    // TODO
}