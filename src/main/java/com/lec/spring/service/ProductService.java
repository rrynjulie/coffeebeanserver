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

    // 기본적인 CRUD
    @Transactional
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product readOne(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Product> readAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product update(Product product) {
        Product productEntity = productRepository.findById(product.getProductId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
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