package com.lec.spring.controller;

import com.lec.spring.domain.Product;
import com.lec.spring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    // 기본적인 CRUD
    @PostMapping("/product/write")
    public ResponseEntity<?> create(@RequestBody Product product) {
        return new ResponseEntity<>(productService.create(product), HttpStatus.CREATED);
    }

    @GetMapping("/product/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(productService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/product/detail/{productId}")
    public ResponseEntity<?> readOne(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.readOne(productId), HttpStatus.OK);
    }

    @PutMapping("/product/update")
    public ResponseEntity<?> update(@RequestBody Product product) {
        return new ResponseEntity<>(productService.update(product), HttpStatus.OK);
    }

    @DeleteMapping("/product/delete/{productId}")
    public ResponseEntity<?> delete(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.delete(productId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}