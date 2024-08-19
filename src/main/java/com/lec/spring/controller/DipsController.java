package com.lec.spring.controller;

import com.lec.spring.domain.Dips;
import com.lec.spring.service.DipsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/dips/delete/{dipsId}")
    public ResponseEntity<?> delete(@PathVariable Long dipsId) {
        return new ResponseEntity<>(dipsService.delete(dipsId), HttpStatus.OK);
    }
}