package com.lec.spring.config;

import com.lec.spring.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

// Outh2 있으면 implemets 추가로 해줄것
public class PrincipalDetails implements UserDetails, OAuth2User  {

    private User user;

    private Map<String, Object> attributes; // OAuth2User의 속성 저장


    public User getUser(){
        return this.user;
    }

    // 일반 로그인 용 생성자
    public PrincipalDetails(User user){
        System.out.println("UserDetails(user) 생성: " + user);
        this.user = user;
    }

    // OAuth2 로그인 용 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("getAuthorities() 호출");

        Collection<GrantedAuthority> collect = new ArrayList<>();

        // 권한이 없을때
        if(user.getRole() == null) return collect;

        // user.getRole()은  "ROLE_MEMBER,ROLE_ADMIN" 과 같은 형태
        Arrays.stream( user.getRole().split(","))
                .forEach(auth -> collect.add(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return auth.trim();
                    }

                    @Override
                    public String toString() {
                        return auth.trim();
                    }
                }));


        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    public String getNickname() {
        return user.getNickName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public LocalDateTime getRegDate() {
        return user.getRegDate();
    }
    public Integer getReliability() {
        return user.getReliability();
    }
    public Integer getMemberStatus() {return user.getMemberStatus(); }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // OAuth2User의 메서드 구현
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getUserName(); // 또는 OAuth2 프로바이더에서 제공하는 유니크한 ID 값
    }



}