package com.lec.spring.controller;

import com.lec.spring.domain.Car;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.domain.enums.Status;
import com.lec.spring.repository.CarRepository;
import com.lec.spring.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class CarController {
    private final CarService carService;
    private final CarRepository carRepository;

    // 기본적인 CRUD
    @PostMapping("/car/write/{userId}")
    public ResponseEntity<?> create(
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("introduce") String introduce,
            @RequestParam("dealingStatus") DealingStatus dealingStatus,
            @RequestParam("category1") String category1,
            @RequestParam("category2") String category2,
            @RequestParam("status") Status status,
            @RequestParam(value = "modelYear", required = false) Integer modelYear,
            @RequestParam(value = "carNum", required = false) String carNum,
            @RequestParam(value = "distance", required = false) Double distance,
            @RequestParam(value = "displacement", required = false) Integer displacement,
            @RequestParam(value = "fuel", required = false) String fuel,
            @RequestParam(value = "transmission", required = false) String transmission,
            @RequestParam(value = "insuranceVictim", required = false) Integer insuranceVictim,
            @RequestParam(value = "insuranceInjurer", required = false) Integer insuranceInjurer,
            @RequestParam(value = "ownerChange", required = false) Integer ownerChange,
            @RequestParam Map<String, MultipartFile> files,
            @PathVariable Long userId
    ) {
        Car carEntity = Car.builder()
                .name(name)
                .price(price)
                .introduce(introduce)
                .dealingStatus(dealingStatus)
                .category1(category1)
                .category2(category2)
                .status(status)
                .modelYear(modelYear)
                .carNum(carNum)
                .distance(distance)
                .displacement(displacement)
                .fuel(fuel)
                .transmission(transmission)
                .insuranceVictim(insuranceVictim)
                .insuranceInjurer(insuranceInjurer)
                .ownerChange(ownerChange)
                .build();
        return new ResponseEntity<>(carService.create(carEntity, userId, files), HttpStatus.CREATED);
    }

    @GetMapping("/car/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(carService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/car/detail/{carId}")
    public ResponseEntity<?> readOne(@PathVariable Long carId) {
        return new ResponseEntity<>(carService.readOne(carId), HttpStatus.OK);
    }

    @GetMapping("/car/category2/{category2}")
    public List<Car> getRandomCarsByCategory2(@PathVariable String category2) {
        return carService.getRandomCarsByCategory2(category2);
    }

    @PutMapping("/car/update/{carId}")
    public ResponseEntity<?> update(
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("introduce") String introduce,
            @RequestParam("dealingStatus") DealingStatus dealingStatus,
            @RequestParam("category1") String category1,
            @RequestParam("category2") String category2,
            @RequestParam("status") Status status,
            @RequestParam(value = "modelYear", required = false) Integer modelYear,
            @RequestParam(value = "carNum", required = false) String carNum,
            @RequestParam(value = "distance", required = false) Double distance,
            @RequestParam(value = "displacement", required = false) Integer displacement,
            @RequestParam(value = "fuel", required = false) String fuel,
            @RequestParam(value = "transmission", required = false) String transmission,
            @RequestParam(value = "insuranceVictim", required = false) Integer insuranceVictim,
            @RequestParam(value = "insuranceInjurer", required = false) Integer insuranceInjurer,
            @RequestParam(value = "ownerChange", required = false) Integer ownerChange,
            @RequestParam Map<String, MultipartFile> files,
            @RequestParam(value = "delfile", required = false) Long[] delfile,
            @PathVariable Long carId
    ) {
        Car carEntity = Car.builder()
                .name(name)
                .price(price)
                .introduce(introduce)
                .dealingStatus(dealingStatus)
                .category1(category1)
                .category2(category2)
                .status(status)
                .modelYear(modelYear)
                .carNum(carNum)
                .distance(distance)
                .displacement(displacement)
                .fuel(fuel)
                .transmission(transmission)
                .insuranceVictim(insuranceVictim)
                .insuranceInjurer(insuranceInjurer)
                .ownerChange(ownerChange)
                .build();
        return new ResponseEntity<>(carService.update(carEntity, carId, files, delfile), HttpStatus.OK);
    }

    @DeleteMapping("/car/delete/{carId}")
    public ResponseEntity<?> delete(@PathVariable Long carId) {
        return new ResponseEntity<>(carService.delete(carId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}