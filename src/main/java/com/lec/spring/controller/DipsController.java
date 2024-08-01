package com.lec.spring.controller;

import com.lec.spring.domain.Dips;
import com.lec.spring.service.DipsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class DipsController {
    private final DipsService dipsService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/dips/write")
    public ResponseEntity<?> create(@RequestBody Dips dips) {
        return new ResponseEntity<>(dipsService.create(dips), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/dips/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(dipsService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/dips/detail/{dipsId}")
    public ResponseEntity<?> readOne(@PathVariable Long dipsId) {
        return new ResponseEntity<>(dipsService.readOne(dipsId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/dips/update")
    public ResponseEntity<?> update(@RequestBody Dips dips) {
        return new ResponseEntity<>(dipsService.update(dips), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/dips/delete/{dipsId}")
    public ResponseEntity<?> delete(@PathVariable Long dipsId) {
        return new ResponseEntity<>(dipsService.delete(dipsId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}