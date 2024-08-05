package com.lec.spring.service;

import com.lec.spring.domain.Product;
import com.lec.spring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;

    // 기본적인 CRUD
    @Transactional
    public Product create(Product product, Long userId) {
        product.setUser(userService.readOne(userId));
        return productRepository.save(product);
    }

    @Transactional
    public Product readOne(Long productId) {
        Product productEntity = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        productEntity.setViewCount(productEntity.getViewCount() + 1);
        return productRepository.save(productEntity);
    }

    @Transactional(readOnly = true)
    public List<Product> readAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product update(Product product) {
        Product productEntity = productRepository.findById(product.getProductId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        productEntity.setName(product.getName());
        productEntity.setDescription(product.getDescription());
        productEntity.setPrice(product.getPrice());
        productEntity.setCategory1(product.getCategory1());
        productEntity.setCategory2(product.getCategory2());
        productEntity.setCategory3(product.getCategory3());
        productEntity.setStatus(product.getStatus());
        productEntity.setDealingType(product.getDealingType());
        productEntity.setDesiredArea(product.getDesiredArea());
        return productEntity;
    }

    @Transactional
    public String delete(Long productId) {
        productRepository.deleteById(productId);
        return "ok";
    }

    // 추가 기능
    // TODO
}