package com.lec.spring.controller;

import com.lec.spring.service.QuitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuitController {
    private final QuitService quitService;
}