package com.lec.spring.repository;

import com.lec.spring.domain.Quit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuitRepository extends JpaRepository<Quit, Long> {
}