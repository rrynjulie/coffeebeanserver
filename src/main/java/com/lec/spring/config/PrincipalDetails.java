package com.lec.spring.config;

import com.lec.spring.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

// Outh2 있으면 implemets 추가로 해줄것
public class PrincipalDetails implements UserDetails {

    private User user;

    public User getUser(){
        return this.user;
    }

    // 일반 로그인 용 생성자
    public PrincipalDetails(User user){
        System.out.println("UserDetails(user) 생성: " + user);
        this.user = user;
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



}