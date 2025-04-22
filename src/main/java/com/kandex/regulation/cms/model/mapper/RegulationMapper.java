package com.kandex.regulation.cms.model.mapper;

import com.kandex.regulation.cms.model.dto.request.RegulationContentRequest;
import com.kandex.regulation.cms.model.dto.request.RegulationRequest;
import com.kandex.regulation.cms.model.dto.response.RegulationResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationsResponse;
import com.kandex.regulation.cms.model.entity.RegulationEntity;
import com.kandex.regulation.cms.model.entity.RegulationContentEntity;
import com.kandex.regulation.cms.model.entity.RegulationRelationshipEntity;
import com.kandex.regulation.cms.model.enums.RegulationType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegulationMapper {
    public static RegulationEntity mapRequestToEntity(RegulationRequest request) {
        RegulationEntity entity = new RegulationEntity();
        entity.setName(request.getName());
        entity.setType(request.getType());
        entity.setTest(request.getTest() == null ? 1 : request.getTest());

        List<RegulationContentRequest> contentRequests = request.getContents();
        if (contentRequests != null && !contentRequests.isEmpty()) {
            Set<RegulationContentEntity> contentEntities =
                    RegulationContentMapper.mapRequestsToEntities(contentRequests);
            contentEntities.forEach(ce -> ce.setRegulation(entity));
            entity.setContents(contentEntities);
        }
        return entity;
    }

    public static RegulationResponse mapEntityToResponse(RegulationEntity entity, boolean includeChildren) {
        RegulationResponse response = mapEntityToResponse(entity);
        if (RegulationType.MAIN.getName().equals(entity.getType()) && includeChildren) {
            Set<RegulationRelationshipEntity> relationshipEntities = entity.getRelationships();
            response.setChildren(mapRelationshipEntitiesToResponseList(relationshipEntities));
        }
        return response;
    }

    public static RegulationResponse mapEntityToResponse(RegulationEntity entity) {
        RegulationResponse response = new RegulationResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setType(entity.getType());
        response.setSort(entity.getSort());
        response.setTest(entity.getTest());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    public static RegulationResponse mapEntityToResponseWithContents(RegulationEntity entity) {
        RegulationResponse response = mapEntityToResponse(entity);
        response.setContents(RegulationContentMapper.mapEntitiesToResponse(entity.getContents()).contents());
        return response;
    }

    public static RegulationsResponse mapEntitiesToResponse(Collection<RegulationEntity> entities, boolean includeChildren, boolean sort) {
        Stream<RegulationResponse> regulationResponseStream = entities.stream()
                .map(e -> mapEntityToResponse(e, includeChildren));
        if (sort) {
            regulationResponseStream = regulationResponseStream.sorted(Comparator.comparing(RegulationResponse::getSort));
        }

        return new RegulationsResponse(regulationResponseStream
                .collect(Collectors.toList()));
    }

    public static RegulationsResponse mapRelationshipEntitiesToResponse(Collection<RegulationRelationshipEntity> relationshipEntities) {
        List<RegulationResponse> regulationResponseList = mapRelationshipEntitiesToResponseList(relationshipEntities);
        return new RegulationsResponse(regulationResponseList);
    }

    private static List<RegulationResponse> mapRelationshipEntitiesToResponseList(Collection<RegulationRelationshipEntity> relationshipEntities) {
        return relationshipEntities.stream()
                .map(relationship -> {
                    RegulationEntity child = relationship.getChild();
                    // we need to set sort value from relationships
                    child.setSort(relationship.getSort());
                    return mapEntityToResponse(child);
                })
                .sorted(Comparator.comparing(RegulationResponse::getSort))
                .toList();
    }
}
