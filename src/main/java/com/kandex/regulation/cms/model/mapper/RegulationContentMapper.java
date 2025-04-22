package com.kandex.regulation.cms.model.mapper;

import com.kandex.regulation.cms.model.dto.request.RegulationContentRequest;
import com.kandex.regulation.cms.model.dto.response.RegulationContentResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationContentsResponse;
import com.kandex.regulation.cms.model.entity.RegulationContentEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegulationContentMapper {
    public static RegulationContentResponse mapEntityToResponse(RegulationContentEntity entity) {
        return new RegulationContentResponse(
                entity.getId(),
                entity.getLang(),
                entity.getTitle(),
                entity.getText()
        );
    }

    public static RegulationContentsResponse mapEntitiesToResponse(Collection<RegulationContentEntity> entities) {
        return new RegulationContentsResponse(entities.stream()
                .map(RegulationContentMapper::mapEntityToResponse)
                .toList());
    }

    public static RegulationContentEntity mapRequestToEntity(RegulationContentRequest request) {
        RegulationContentEntity entity = new RegulationContentEntity();
        entity.setTitle(request.getTitle());
        entity.setLang(request.getLang());
        entity.setText(request.getText());
        return entity;
    }

    public static Set<RegulationContentEntity> mapRequestsToEntities(List<RegulationContentRequest> requests) {
        return requests.stream()
                .map(RegulationContentMapper::mapRequestToEntity)
                .collect(Collectors.toSet());
    }
}
