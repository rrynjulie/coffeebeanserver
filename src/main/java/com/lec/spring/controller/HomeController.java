package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Home";
    }

    // TODO
    // 홈 컨트롤러에 추가 하시고 싶으신 분들은 여기에 추가로 작성 요망

    @GetMapping("/admin")
    public String admin(){
        return "admin page";
    }

    @GetMapping("/member")
    public String member(){
        return "member page";
    }

    // 현재 Authentication 보기
    @RequestMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
        // SecurityContextHolder 안에 있는 getAuthentication 꺼내오기
        // Authentication 이 인증된 정보이기 때문에 SecurityContextHolder 가 가지고 있는 값을 통해 인증이 되었는지 확인할 수 잇다.
    }

    @RequestMapping("/user")
    public User user(@AuthenticationPrincipal PrincipalDetails userDetails){
        return (userDetails != null) ? userDetails.getUser() : null;
    }

}
