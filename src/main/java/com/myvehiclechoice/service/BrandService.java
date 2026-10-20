package com.myvehiclechoice.service;

import com.myvehiclechoice.dto.BrandDto;
import com.myvehiclechoice.model.entity.Brand;
import com.myvehiclechoice.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandDto> getAllActiveBrands() {
        return brandRepository.findByIsActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private BrandDto toDto(Brand brand) {
        return new BrandDto(
                brand.getId(),
                brand.getName(),
                brand.getCountry(),
                brand.getLogoUrl(),
                brand.getIsActive()
        );
    }
}
