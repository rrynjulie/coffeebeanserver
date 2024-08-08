package com.lec.spring.controller;

import com.lec.spring.domain.Product;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.domain.enums.Status;
import com.lec.spring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    // 기본적인 CRUD
//    @PostMapping("/product/write/{userId}")
//    public ResponseEntity<?> create(@RequestBody Product product, @PathVariable Long userId, @RequestParam Map<String, MultipartFile> files) {
//        return new ResponseEntity<>(productService.create(product, userId, files), HttpStatus.CREATED);
//    }

    @PostMapping("/product/write/{userId}")
    public ResponseEntity<Integer> create(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") int price,
            @RequestParam("dealingStatus") DealingStatus dealingStatus,
            @RequestParam("category1") String category1,
            @RequestParam("category2") String category2,
            @RequestParam("category3") String category3,
            @RequestParam("status") Status status,
            @RequestParam("dealingType") String dealingType,
            @RequestParam("desiredArea") String desiredArea,
            @RequestParam Map<String, MultipartFile> files,
            @PathVariable Long userId
    ) {
        Product productEntity = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .dealingStatus(dealingStatus)
                .category1(category1)
                .category2(category2)
                .category3(category3)
                .status(status)
                .dealingType(dealingType)
                .desiredArea(desiredArea)
                .build();
        return new ResponseEntity<>(productService.create(productEntity, userId, files), HttpStatus.CREATED);
    }

    @GetMapping("/product/list")
    public ResponseEntity<?> readAll() {
        return new ResponseEntity<>(productService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/product/detail/{productId}")
    public ResponseEntity<?> readOne(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.readOne(productId), HttpStatus.OK);
    }

    @PutMapping("/product/update/{productId}")
    public ResponseEntity<Integer> update(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") int price,
//            @RequestParam("dealingStatus") DealingStatus dealingStatus,
            @RequestParam("category1") String category1,
            @RequestParam("category2") String category2,
            @RequestParam("category3") String category3,
            @RequestParam("status") Status status,
            @RequestParam("dealingType") String dealingType,
            @RequestParam("desiredArea") String desiredArea,
            @RequestParam Map<String, MultipartFile> files,
            @RequestParam(value = "delfile", required = false) Long[] delfile,
            @PathVariable Long productId
    ) {
        Product productEntity = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .category1(category1)
                .category2(category2)
                .category3(category3)
                .status(status)
                .dealingType(dealingType)
                .desiredArea(desiredArea)
                .build();
        return new ResponseEntity<>(productService.update(productEntity, productId, files, delfile), HttpStatus.OK);
    }

    @DeleteMapping("/product/delete/{productId}")
    public ResponseEntity<?> delete(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.delete(productId), HttpStatus.OK);
    }

    // 추가 기능
    // TODO
}