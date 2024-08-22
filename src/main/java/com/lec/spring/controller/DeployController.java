package com.lec.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class DeployController {
    @GetMapping("/deploy")
    public ResponseEntity<?> checkDeploy() {
        return new ResponseEntity<>("Spring Server 배포 정상 가동 중", HttpStatus.OK);
    }
}