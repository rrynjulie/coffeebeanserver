package com.lec.spring.controller;

import com.lec.spring.domain.Post;
import com.lec.spring.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    // 기본적인 CRUD
    @CrossOrigin
    @PostMapping("/post/write")
    public ResponseEntity<?> create(@RequestBody Post post, @RequestParam Map<String, MultipartFile> files, BindingResult result) {
        return new ResponseEntity<>(postService.create(post, files), HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/post/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(postService.readAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/post/detail/{postId}")
    public ResponseEntity<?> readOne(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.readOne(postId), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/post/update")
    public ResponseEntity<?> update(@RequestBody Post post, @RequestParam Map<String, MultipartFile> files) {
        return new ResponseEntity<>(postService.update(post, files), HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/post/delete/{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId) {
        return new ResponseEntity<>(postService.delete(postId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}