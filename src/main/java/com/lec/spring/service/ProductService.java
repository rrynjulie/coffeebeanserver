package com.lec.spring.service;

import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.ChatRoom;
import com.lec.spring.domain.Product;
import com.lec.spring.domain.enums.DealingStatus;
import com.lec.spring.repository.ChatRoomRepository;
import com.lec.spring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final AttachmentService attachmentService;
    private final ChatRoomRepository chatRoomRepository;

    // 기본적인 CRUD
    @Transactional
    public long create(Product product, Long userId, MultipartFile[] files) {
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
    public long update(Product product, Long productId, MultipartFile[] files, Long[] delfile) {
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
    public List<Product> getProductsByCategory(String category1, String category2, String category3){
        return productRepository.findAll().stream()
                .filter(product -> (category1 == null || product.getCategory1().equals(category1)) &&
                        (category2 == null || product.getCategory2().equals(category2)) &&
                        (category3) == null || product.getCategory3().equals(category3))
                .toList();
    }

    // 헤더에서 사용하는 검색 결과 불러오는 메소드
    @Transactional(readOnly = true)
    public List<Product> readAllByKeyword(String keyword) {
        List<Product> productList =  productRepository
                .findAll()
                .stream()
                .filter(product -> product.getName().contains(keyword))
                .collect(Collectors.toList());
        return productList;
    }

    // 마이페이지에서 사용하는 모든 필터 한 번에 걸러주는 메소드
    @Transactional(readOnly = true)
    public List<Product> readAllSellsByUserSorted(Long userId, int sortType, String dealingStatus) {
        Sort sort;
        if(sortType == 1) sort = Sort.by(Sort.Order.desc("regDate"));
        else if(sortType == 2) sort = Sort.by(Sort.Order.asc("price"));
        else sort = Sort.by(Sort.Order.desc("price"));
        List<Product> productList = productRepository.findByUser_userId(userId, sort);

        if(dealingStatus.equals("전체")) return productList;
        DealingStatus tempDS = DealingStatus.valueOf(dealingStatus);
        return productList
                .stream()
                .filter(product -> product.getDealingStatus().equals(tempDS))
                .collect(Collectors.toList());
    }

    // 중고 물품 상세 페이지에서 사용하는 판매 상태 변경해주는 메소드
    @Transactional
    public Product updateDealingStatus(Long productId, DealingStatus dealingStatus) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        product.setDealingStatus(dealingStatus);

        if (dealingStatus == DealingStatus.판매완료) {
            List<ChatRoom> chatRooms = chatRoomRepository.findByProductId(productId);
            for (ChatRoom chatRoom : chatRooms) {
                chatRoom.setDealComplete(true);
                chatRoomRepository.save(chatRoom);
            }
        }
        return productRepository.saveAndFlush(product);
    }

    @Transactional
    public List<Product> getTop10ByViewCnt() {
        return productRepository.findTop10ByDealingStatusOrderByViewCountDesc(DealingStatus.판매중);
    }

    @Transactional
    public List<Product> getTop10ByRegDate() {
        return productRepository.findTop10ByDealingStatusOrderByRegDateDesc(DealingStatus.판매중);
    }


    public Map<String, Object> getPriceInfoCategory(String category1, String category2, String category3) {
        List<Product> products = productRepository.findProductsByCategories(category1 ,category2, category3);

        // 가격 리스트
        List<Double> prices = new ArrayList<>();
        for (Product product : products) {
            // 가격을 int에서 Double로 변환
            prices.add((double) product.getPrice());
        }

        // 평균, 최소, 최대 가격 계산
        double averagePrice = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double minPrice = prices.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double maxPrice = prices.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

        // 결과를 Map으로 반환
        Map<String, Object> priceInfo = new HashMap<>();
        priceInfo.put("prices", prices);
        priceInfo.put("averagePrice", averagePrice);
        priceInfo.put("minPrice", minPrice);
        priceInfo.put("maxPrice", maxPrice);
        priceInfo.put("productCount", products.size());

        return priceInfo;
    }

//    public Map<String, Object> getPriceInfoByCategories(String category1, String category2, String category3) {
//        List<Product> products;
//        if (category1 != null && category2 != null && category3 != null) {
//            products = productRepository.findByCategory1AndCategory2AndCategory3(category1, category2, category3);
//        } else if (category1 != null && category2 != null) {
//            products = productRepository.findByCategory1AndCategory2AndCategory3(category1, category2, null);
//        } else if (category1 != null) {
//            products = productRepository.findByCategory1AndCategory2AndCategory3(category1, null, null);
//        } else {
//            products = productRepository.findByCategory1AndCategory2AndCategory3(null, null, null);
//        }
//
//        // 가격 리스트
//        List<Double> prices = products.stream()
//                .map(product -> (double) product.getPrice())
//                .toList();
//
//        // 평균, 최소, 최대 가격 계산
//        double averagePrice = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
//        double minPrice = prices.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
//        double maxPrice = prices.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
//
//        // 결과를 Map으로 반환
//        Map<String, Object> priceInfo = new HashMap<>();
//        priceInfo.put("prices", prices);
//        priceInfo.put("averagePrice", averagePrice);
//        priceInfo.put("minPrice", minPrice);
//        priceInfo.put("maxPrice", maxPrice);
//        priceInfo.put("productCount", products.size());
//
//        return priceInfo;
//    }

}