package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Product;
import com.lec.spring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final AttachmentService attachmentService;

    // 기본적인 CRUD
    @Transactional
    public long create(Product product, Long userId, Map<String, MultipartFile> files) {
        product.setUser(userService.readOne(userId));
        product = productRepository.saveAndFlush(product);
        attachmentService.addFiles(files, product);
        return product.getProductId();
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
    public long update(Product product, Long productId, Map<String, MultipartFile> files, Long[] delfile) {
        Product productEntity = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));

        productEntity.setName(product.getName());
        productEntity.setDescription(product.getDescription());
        productEntity.setPrice(product.getPrice());
        productEntity.setCategory1(product.getCategory1());
        productEntity.setCategory2(product.getCategory2());
        productEntity.setCategory3(product.getCategory3());
        productEntity.setStatus(product.getStatus());
        productEntity.setDealingType(product.getDealingType());
        productEntity.setDesiredArea(product.getDesiredArea());

        productEntity = productRepository.saveAndFlush(productEntity);
        attachmentService.addFiles(files, productEntity);

        if(delfile != null) {
            for(Long fileId : delfile) {
                Attachment file = attachmentService.readOne(fileId);
                if(file != null) {
                    attachmentService.delFile(file);
                    attachmentService.delete(fileId);
                }
            }
        }

        return productId;
    }

    @Transactional
    public String delete(Long productId) {
        productRepository.deleteById(productId);
        return "ok";
    }

    // 추가 기능
    // TODO
}