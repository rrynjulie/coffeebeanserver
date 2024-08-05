package com.lec.spring.service;

import com.lec.spring.domain.Dips;
import com.lec.spring.repository.DipsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DipsService {
    private final DipsRepository dipsRepository;
    private final UserService userService;

    // 기본적인 CRUD
    @Transactional
    public Dips create(Dips dips, Long userId) {
        dips.setUser(userService.readOne(userId));
        return dipsRepository.save(dips);
    }

    @Transactional(readOnly = true)
    public List<Dips> readAll() {
        return dipsRepository.findAll();
    }

    @Transactional
    public String delete(Long dipsId) {
        dipsRepository.deleteById(dipsId);
        return "ok";
    }

    // 추가 기능
    // TODO
}