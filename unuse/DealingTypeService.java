package com.lec.spring.service;

import com.lec.spring.domain.DealingType;
import com.lec.spring.repository.DealingTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DealingTypeService {
    private final DealingTypeRepository dealingTypeRepository;

    // 기본적인 CRUD
    @Transactional
    public DealingType create(DealingType dealingType) {
        return dealingTypeRepository.save(dealingType);
    }

    @Transactional(readOnly = true)
    public DealingType readOne(Long dealingTypeId) {
        return dealingTypeRepository.findById(dealingTypeId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<DealingType> readByPropertyId(Long propertyId) {
        return dealingTypeRepository.findByProperty_propertyId(propertyId);
    }

    @Transactional
    public DealingType update(DealingType dealingType) {
        DealingType dealingTypeEntity = dealingTypeRepository.findById(dealingType.getDealingTypeId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return dealingTypeEntity;
    }

    @Transactional
    public String delete(Long dealingTypeId) {
        dealingTypeRepository.deleteById(dealingTypeId);
        return "ok";
    }

    // 추가 기능
    // TODO
}