package com.lec.spring.service;

import com.lec.spring.domain.Quit;
import com.lec.spring.repository.QuitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuitService {
    private final QuitRepository quitRepository;

    // 기본적인 CRUD
    @Transactional
    public Quit create(Quit quit) {
        return quitRepository.save(quit);
    }

    @Transactional(readOnly = true)
    public Quit readOne(Long quitId) {
        return quitRepository.findById(quitId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Quit> readAll() {
        return quitRepository.findAll();
    }

    @Transactional
    public Quit update(Quit quit) {
        Quit quitEntity = quitRepository.findById(quit.getQuitId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return quitEntity;
    }

    @Transactional
    public String delete(Long quitId) {
        quitRepository.deleteById(quitId);
        return "ok";
    }

    // 추가 기능
    // TODO
}