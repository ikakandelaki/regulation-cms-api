package com.kandex.regulation.cms.service.impl;

import com.kandex.regulation.cms.exception.unchecked.BaseUncheckedException;
import com.kandex.regulation.cms.model.dto.request.RegulationLanguageRequest;
import com.kandex.regulation.cms.model.dto.response.RegulationLanguageResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationLanguagesResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationTypesResponse;
import com.kandex.regulation.cms.model.entity.RegulationLanguageEntity;
import com.kandex.regulation.cms.model.entity.RegulationTypeEntity;
import com.kandex.regulation.cms.model.mapper.RegulationLanguageMapper;
import com.kandex.regulation.cms.repository.RegulationLanguageRepository;
import com.kandex.regulation.cms.repository.RegulationTypeRepository;
import com.kandex.regulation.cms.service.RegulationMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegulationMetadataServiceImpl implements RegulationMetadataService {
    private final RegulationTypeRepository regulationTypeRepository;
    private final RegulationLanguageRepository regulationLanguageRepository;

    @Override
    public RegulationTypesResponse getRegulationTypes() {
        List<RegulationTypeEntity> entities = regulationTypeRepository.findAll();
        return new RegulationTypesResponse(entities.stream()
                .map(RegulationTypeEntity::getName)
                .toList()
        );
    }

    @Override
    public RegulationLanguagesResponse getRegulationLanguages() {
        List<RegulationLanguageEntity> entities = regulationLanguageRepository.findAll();
        return RegulationLanguageMapper.mapEntitiesToResponse(entities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegulationLanguageResponse addRegulationLanguage(RegulationLanguageRequest request) {
        RegulationLanguageEntity entity = RegulationLanguageMapper.mapRequestToEntity(request);
        RegulationLanguageEntity savedEntity = regulationLanguageRepository.save(entity);
        return RegulationLanguageMapper.mapEntityToResponse(savedEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRegulationLanguage(String code) {
        regulationLanguageRepository.findById(code)
                .ifPresentOrElse(regulationLanguageRepository::delete,
                        () -> {
                            throw new BaseUncheckedException("Language does not exist");
                        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegulationLanguageResponse editRegulationLanguageName(String code, String name) {
        RegulationLanguageEntity entity = regulationLanguageRepository.findById(code)
                .orElseThrow(() -> new BaseUncheckedException("Language does not exist"));

        if (!name.equals(entity.getName())) {
            regulationLanguageRepository.findByName(name).ifPresent(e -> {
                throw new BaseUncheckedException("Other language with such name already exists. Name should be unique");
            });
            entity.setName(name);
        }

        return RegulationLanguageMapper.mapEntityToResponse(entity);
    }
}
