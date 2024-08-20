package com.lec.spring.controller;

import com.lec.spring.domain.Car;
import com.lec.spring.domain.Product;
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
    public ResponseEntity<Long> create(
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
            @RequestParam("files") MultipartFile[] files,
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
    public ResponseEntity<Long> update(
            @PathVariable Long carId,  // PathVariable을 사용하여 carId를 받습니다.
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
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam(value = "delfile", required = false) Long[] delfile
    ) {
        System.out.println("Received carId: " + carId);  // carId 확인

        if (carId == null) {
            throw new IllegalArgumentException("carId must not be null");
        }

        // Car 엔티티 생성
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
    @GetMapping("/car/filter")
    public ResponseEntity<List<Car>> filterCars(
            @RequestParam(required = false) String category1,
            @RequestParam(required = false) String category2
    ) {
        List<Car> cars = carService.getFilteredCars(category1, category2);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    // 헤더에서 사용하는 검색 결과 불러오는 메소드
    @GetMapping("/car/list/{keyword}")
    public ResponseEntity<?> readAllByKeyword(@PathVariable String keyword) {
        return new ResponseEntity<>(carService.readAllByKeyword(keyword), HttpStatus.OK);
    }

    // 마이페이지에서 사용하는 모든 필터 한 번에 걸러주는 메소드
    @GetMapping("/sell/car/sortedlist/{userId}/{sortedType}/{dealingStatus}")
    public ResponseEntity<?> readAllByUserSorted(@PathVariable Long userId,
                                                 @PathVariable int sortedType,
                                                 @PathVariable String dealingStatus
    ) {
        return new ResponseEntity<>(carService.readAllByUserSorted(userId, sortedType, dealingStatus), HttpStatus.OK);
    }

    // 중고차 상세 페이지에서 사용하는 판매 상태 변경해주는 메소드
    @PutMapping("/car/update/status/{carId}")
    public ResponseEntity<?> updateDealingStatus(
            @RequestParam("dealingStatus") DealingStatus dealingStatus,
            @PathVariable Long carId
    ) {
        return new ResponseEntity<>(carService.updateDealingStatus(carId, dealingStatus), HttpStatus.OK);
    }

    //실시간 인기 중고상품(조회수 TOP10)
    @GetMapping("/car/top10")
    public ResponseEntity<List<Car>> carTop10() {
        List<Car> topCar = carService.getTop10ByViewCnt();
        return new ResponseEntity<>(topCar, HttpStatus.OK);
    }
}