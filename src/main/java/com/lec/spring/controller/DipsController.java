package com.lec.spring.controller;

import com.lec.spring.domain.Dips;
import com.lec.spring.service.DipsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class DipsController {
    private final DipsService dipsService;

    // 기본적인 CRUD
    @PostMapping("/dips/write/{entityType}/{userId}/{entityId}")
    public ResponseEntity<?> create(
            @PathVariable String entityType,
            @PathVariable Long userId,
            @PathVariable Long entityId
    ) {
        Dips dipsEntity = Dips.builder().build();
        return new ResponseEntity<>(dipsService.create(dipsEntity, entityType, userId, entityId), HttpStatus.CREATED);
    }

    @GetMapping("/dips/{entityType}/sortedlist/{userId}/{sortedType}/{dealingStatus}")
    public ResponseEntity<?> readByUserId(
            @PathVariable String entityType,
            @PathVariable Long userId,
            @PathVariable int sortedType,
            @PathVariable String dealingStatus
    ) {
        return new ResponseEntity<>(dipsService.readByUserId(userId, entityType, sortedType, dealingStatus), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{entityType}/{userId}/{entityId}")
    public ResponseEntity<String> delete(
            @PathVariable String entityType,
            @PathVariable Long userId,
            @PathVariable Long entityId) {

        boolean result = dipsService.delete(userId, entityId, entityType);

        if (result) {
            return ResponseEntity.ok("찜 항목이 성공적으로 삭제되었습니다.");
        } else {
            return ResponseEntity.status(404).body("해당 찜 항목이 존재하지 않거나 삭제에 실패했습니다.");
        }
    }

    //찜 상태 (product)
    @GetMapping("/dips/status")
    public ResponseEntity<?> checkDipsStatus(@RequestParam Long userId, @RequestParam Long productId) {
        boolean isDipped = dipsService.isProductDippedByUser(userId, productId);
        return new ResponseEntity<>(Collections.singletonMap("isDipped", isDipped), HttpStatus.OK);
    }

    //찜 상태(car)
    @GetMapping("/dips/status/car")
    public ResponseEntity<?> checkDipsStatusCar(@RequestParam Long userId, @RequestParam Long carId) {
        boolean isDipped = dipsService.isCarDippedByUser(userId, carId);
        return new ResponseEntity<>(Collections.singletonMap("isDipped", isDipped), HttpStatus.OK);
    }


}