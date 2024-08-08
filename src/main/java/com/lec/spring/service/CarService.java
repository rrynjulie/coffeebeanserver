package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Car;
import com.lec.spring.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserService userService;
    private final AttachmentService attachmentService;

    // 기본적인 CRUD
    @Transactional
    public int create(Car car, Long userId, Map<String, MultipartFile> files) {
        car.setUser(userService.readOne(userId));
        car = carRepository.saveAndFlush(car);
        attachmentService.addFiles(files, car);
        return 1;
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
    public int update(Car car, Long carId, Map<String, MultipartFile> files, Long[] delfile) {
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

        return 1;
    }

    @Transactional
    public String delete(Long carId) {
        carRepository.deleteById(carId);
        return "ok";
    }

    // 추가 기능
    // TODO
}