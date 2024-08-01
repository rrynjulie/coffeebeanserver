package com.lec.spring.controller;

import com.lec.spring.domain.ChatRoom;
import com.lec.spring.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/chatRoom/write")
    public ResponseEntity<?> create(@RequestBody ChatRoom chatRoom) {
        return new ResponseEntity<>(chatRoomService.create(chatRoom), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/chatRoom/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(chatRoomService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/chatRoom/detail/{chatRoomId}")
    public ResponseEntity<?> readOne(@PathVariable Long chatRoomId) {
        return new ResponseEntity<>(chatRoomService.readOne(chatRoomId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/chatRoom/update")
    public ResponseEntity<?> update(@RequestBody ChatRoom chatRoom) {
        return new ResponseEntity<>(chatRoomService.update(chatRoom), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/chatRoom/delete/{chatRoomId}")
    public ResponseEntity<?> delete(@PathVariable Long chatRoomId) {
        return new ResponseEntity<>(chatRoomService.delete(chatRoomId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}