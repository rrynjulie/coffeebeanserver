package com.lec.spring.repository;

import com.lec.spring.domain.DealingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealingTypeRepository extends JpaRepository<DealingType, Long> {
    List<DealingType> findByProperty_propertyId(Long propertyId);
}