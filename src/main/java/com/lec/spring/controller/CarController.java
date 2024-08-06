package com.lec.spring.controller;

import com.lec.spring.domain.Car;
import com.lec.spring.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CarController {
    private final CarService carService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/car/write/{userId}")
    public ResponseEntity<?> create(@RequestBody Car car, @PathVariable Long userId) {
        return new ResponseEntity<>(carService.create(car, userId), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/car/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(carService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/car/detail/{carId}")
    public ResponseEntity<?> readOne(@PathVariable Long carId) {
        return new ResponseEntity<>(carService.readOne(carId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/car/update")
    public ResponseEntity<?> update(@RequestBody Car car) {
        return new ResponseEntity<>(carService.update(car), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/car/delete/{carId}")
    public ResponseEntity<?> delete(@PathVariable Long carId) {
        return new ResponseEntity<>(carService.delete(carId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}