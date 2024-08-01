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

    // 기본적인 CRUD
    @Transactional
    public Dips create(Dips dips) {
        return dipsRepository.save(dips);
    }

    @Transactional(readOnly = true)
    public Dips readOne(Long dipsId) {
        return dipsRepository.findById(dipsId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Dips> readAll() {
        return dipsRepository.findAll();
    }

    @Transactional
    public Dips update(Dips dips) {
        Dips dipsEntity = dipsRepository.findById(dips.getDipsId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return dipsEntity;
    }

    @Transactional
    public String delete(Long dipsId) {
        dipsRepository.deleteById(dipsId);
        return "ok";
    }

    // 추가 기능
    // TODO
}