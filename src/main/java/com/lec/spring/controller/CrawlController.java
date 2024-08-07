package com.lec.spring.controller;

import com.lec.spring.service.CrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlController {

    @Autowired
    private CrawlingService crawlingService;

    @GetMapping("/crawlproduct")
    public String crawlproduct() {
        crawlingService.saveAllProducts();
        return "중고 물품 상세 정보 크롤링 완료";
    }
}
