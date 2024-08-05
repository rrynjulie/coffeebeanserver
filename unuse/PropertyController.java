package com.lec.spring.controller;

import com.lec.spring.domain.Property;
import com.lec.spring.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PropertyController {
    private final PropertyService propertyService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/property/write/{userId}")
    public ResponseEntity<?> create(@RequestBody Property property, @PathVariable Long userId) {
        return new ResponseEntity<>(propertyService.create(property, userId), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/property/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(propertyService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/property/detail/{propertyId}")
    public ResponseEntity<?> readOne(@PathVariable Long propertyId) {
        return new ResponseEntity<>(propertyService.readOne(propertyId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/property/update")
    public ResponseEntity<?> update(@RequestBody Property property) {
        return new ResponseEntity<>(propertyService.update(property), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/property/delete/{propertyId}")
    public ResponseEntity<?> delete(@PathVariable Long propertyId) {
        return new ResponseEntity<>(propertyService.delete(propertyId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}