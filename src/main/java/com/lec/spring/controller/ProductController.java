package com.lec.spring.controller;

import com.lec.spring.domain.Product;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.domain.enums.Status;
import com.lec.spring.service.ChatRoomService;
import com.lec.spring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class ProductController {
    private final ProductService productService;
    private final ChatRoomService chatRoomService;

    // 기본적인 CRUD
//    @PostMapping("/product/write/{userId}")
//    public ResponseEntity<?> create(@RequestBody Product product, @PathVariable Long userId, @RequestParam Map<String, MultipartFile> files) {
//        return new ResponseEntity<>(productService.create(product, userId, files), HttpStatus.CREATED);
//    }

    @PostMapping("/product/write/{userId}")
    public ResponseEntity<Long> create(
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
            @RequestParam("files") MultipartFile[] files,
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
    public ResponseEntity<Long> update(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") int price,
            @RequestParam("category1") String category1,
            @RequestParam("category2") String category2,
            @RequestParam("category3") String category3,
            @RequestParam("status") Status status,
            @RequestParam("dealingType") String dealingType,
            @RequestParam("desiredArea") String desiredArea,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
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

    // 헤더에서 사용하는 검색 결과 불러오는 메소드
    @GetMapping("/product/list/{keyword}")
    public ResponseEntity<?> readAllByKeyword(@PathVariable String keyword) {
        return new ResponseEntity<>(productService.readAllByKeyword(keyword), HttpStatus.OK);
    }

    // 마이페이지 판매목록
    @GetMapping("/sell/product/sortedlist/{userId}/{sortedType}/{dealingStatus}")
    public ResponseEntity<?> readSellsByUserSorted(@PathVariable Long userId,
                                                 @PathVariable int sortedType,
                                                 @PathVariable String dealingStatus) {
        return new ResponseEntity<>(productService.readAllSellsByUserSorted(userId, sortedType, dealingStatus), HttpStatus.OK);
    }

    // 마이페이지 구매목록
    @GetMapping("/buy/{entityType}/sortedlist/{userId}/{sortedType}/{dealingStatus}")
    public ResponseEntity<?> readBuysByUserSorted(
            @PathVariable String entityType,
            @PathVariable Long userId,
            @PathVariable int sortedType,
            @PathVariable String dealingStatus
    ) {
        return new ResponseEntity<>(chatRoomService.readByUserId(userId, entityType, sortedType, dealingStatus), HttpStatus.OK);
    }

    // 중고 물품 상세 페이지에서 사용하는 판매 상태 변경해주는 메소드
    @PutMapping("/product/update/status/{productId}")
    public ResponseEntity<?> updateDealingStatus(
            @RequestParam("dealingStatus") DealingStatus dealingStatus,
            @PathVariable Long productId
    ) {
        return new ResponseEntity<>(productService.updateDealingStatus(productId, dealingStatus), HttpStatus.OK);
    }

    @GetMapping("/product/priceInfo")
    public Map<String, Object> getPriceInfoByCategory(
            @RequestParam(required = false) String category1,
            @RequestParam(required = false) String category2,
            @RequestParam(required = false) String category3) {

        return productService.getPriceInfoCategory(category1, category2, category3);
    }

    @GetMapping("/product/priceInfo/{keyword}")
    public Map<String, Object> getPriceInfoBySearch(@PathVariable String keyword) {
        return productService.getPriceInfoSearch(keyword);
    }

//    @GetMapping("/product/priceInfo")
//    public Map<String, Object> getPriceInfo(
//            @RequestParam(required = false) String category1,
//            @RequestParam(required = false) String category2,
//            @RequestParam(required = false) String category3) {
//
//        return productService.getPriceInfoByCategories(category1, category2, category3);
//    }


    @GetMapping("/product/category")
    public List<Product> getProducts(
            @RequestParam(required = false) String category1,
            @RequestParam(required = false) String category2,
            @RequestParam(required = false) String category3) {
        return productService.getProductsByCategory(category1,category2,category3);
    }




    //실시간 인기 중고상품(조회수 TOP10)
    @GetMapping("/product/top10")
    public ResponseEntity<List<Product>> productTop10() {
        List<Product> topproduct = productService.getTop10ByViewCnt();
        return new ResponseEntity<>(topproduct, HttpStatus.OK);
    }

    //최근에 등록된 상품
    @GetMapping("/product/top10regDate")
    public ResponseEntity<List<Product>> productRecentTop10() {
        List<Product> top10regDate = productService.getTop10ByRegDate();
        return new ResponseEntity<>(top10regDate, HttpStatus.OK);
    }
}