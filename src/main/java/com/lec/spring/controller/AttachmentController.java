package com.lec.spring.controller;

import com.lec.spring.domain.Attachment;
import com.lec.spring.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AttachmentController {
    private final AttachmentService attachmentService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/attachment/write")
    public ResponseEntity<?> create(@RequestBody Attachment attachment) {
        return new ResponseEntity<>(attachmentService.create(attachment), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/attachment/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(attachmentService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/attachment/detail/{attachmentId}")
    public ResponseEntity<?> readOne(@PathVariable Long attachmentId) {
        return new ResponseEntity<>(attachmentService.readOne(attachmentId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/attachment/update")
    public ResponseEntity<?> update(@RequestBody Attachment attachment) {
        return new ResponseEntity<>(attachmentService.update(attachment), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/attachment/delete/{attachmentId}")
    public ResponseEntity<?> delete(@PathVariable Long attachmentId) {
        return new ResponseEntity<>(attachmentService.delete(attachmentId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}