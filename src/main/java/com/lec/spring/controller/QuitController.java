package com.lec.spring.controller;

import com.lec.spring.domain.Quit;
import com.lec.spring.service.QuitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class QuitController {
    private final QuitService quitService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/quit/write/{userId}")
    public ResponseEntity<?> create(@RequestBody Quit quit, @PathVariable Long userId) {
        return new ResponseEntity<>(quitService.create(quit, userId), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/quit/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(quitService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/quit/detail/{quitId}")
    public ResponseEntity<?> readOne(@PathVariable Long quitId) {
        return new ResponseEntity<>(quitService.readOne(quitId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/quit/update")
    public ResponseEntity<?> update(@RequestBody Quit quit) {
        return new ResponseEntity<>(quitService.update(quit), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/quit/delete/{quitId}")
    public ResponseEntity<?> delete(@PathVariable Long quitId) {
        return new ResponseEntity<>(quitService.delete(quitId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}