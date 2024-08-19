package com.lec.spring.repository;

import com.lec.spring.domain.Dips;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DipsRepository extends JpaRepository<Dips, Long> {
    List<Dips> findByUser_userId(Long userId);
}