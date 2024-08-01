package com.lec.spring.repository;

import com.lec.spring.domain.Dips;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DipsRepository extends JpaRepository<Dips, Long> {
}