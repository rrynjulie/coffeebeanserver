package com.lec.spring.service;

import com.lec.spring.domain.DealingType;
import com.lec.spring.domain.Property;
import com.lec.spring.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserService userService;

    // 기본적인 CRUD
    @Transactional
    public Property create(Property property, Long userId) {
        property.setUser(userService.readOne(userId));
        if(property.getDealingTypes() != null) {
            for(DealingType dealingType : property.getDealingTypes())
                dealingType.setProperty(property);
        }
        return propertyRepository.save(property);
    }

    @Transactional(readOnly = true)
    public Property readOne(Long propertyId) {
        return propertyRepository.findById(propertyId).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Property> readAll() {
        return propertyRepository.findAll();
    }

    @Transactional
    public Property update(Property property) {
        Property propertyEntity = propertyRepository.findById(property.getPropertyId()).orElseThrow(() -> new IllegalArgumentException("ID를 확인해주세요."));
        // TODO
        return propertyEntity;
    }

    @Transactional
    public String delete(Long propertyId) {
        propertyRepository.deleteById(propertyId);
        return "ok";
    }

    // 추가 기능
    // TODO
}