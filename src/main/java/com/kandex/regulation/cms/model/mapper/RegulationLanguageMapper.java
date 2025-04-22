package com.kandex.regulation.cms.model.mapper;

import com.kandex.regulation.cms.model.dto.request.RegulationLanguageRequest;
import com.kandex.regulation.cms.model.dto.response.RegulationLanguageResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationLanguagesResponse;
import com.kandex.regulation.cms.model.entity.RegulationLanguageEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegulationLanguageMapper {
    public static RegulationLanguageEntity mapRequestToEntity(RegulationLanguageRequest request) {
        RegulationLanguageEntity entity = new RegulationLanguageEntity();
        entity.setCode(request.getCode());
        entity.setName(request.getName());
        return entity;
    }

    public static RegulationLanguageResponse mapEntityToResponse(RegulationLanguageEntity entity) {
        return new RegulationLanguageResponse(
                entity.getCode(),
                entity.getName()
        );
    }

    public static RegulationLanguagesResponse mapEntitiesToResponse(List<RegulationLanguageEntity> entityList) {
        return new RegulationLanguagesResponse(entityList.stream()
                .map(RegulationLanguageMapper::mapEntityToResponse)
                .toList());
    }
}
