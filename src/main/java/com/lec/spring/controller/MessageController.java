package com.lec.spring.controller;

import com.lec.spring.domain.Message;
import com.lec.spring.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MessageController {
    private final MessageService messageService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/message/write")
    public ResponseEntity<?> create(@RequestBody Message message) {
        return new ResponseEntity<>(messageService.create(message), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/message/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(messageService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/message/detail/{messageId}")
    public ResponseEntity<?> readOne(@PathVariable Long messageId) {
        return new ResponseEntity<>(messageService.readOne(messageId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/message/update")
    public ResponseEntity<?> update(@RequestBody Message message) {
        return new ResponseEntity<>(messageService.update(message), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/message/delete/{messageId}")
    public ResponseEntity<?> delete(@PathVariable Long messageId) {
        return new ResponseEntity<>(messageService.delete(messageId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}