package com.kandex.regulation.cms.service.impl;

import com.kandex.regulation.cms.exception.unchecked.BaseUncheckedException;
import com.kandex.regulation.cms.model.dto.request.RegulationContentRequest;
import com.kandex.regulation.cms.model.dto.request.RegulationRequest;
import com.kandex.regulation.cms.model.dto.response.RegulationContentResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationContentsResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationsResponse;
import com.kandex.regulation.cms.model.entity.RegulationContentEntity;
import com.kandex.regulation.cms.model.entity.RegulationEntity;
import com.kandex.regulation.cms.model.entity.RegulationRelationshipEntity;
import com.kandex.regulation.cms.model.entity.RegulationRelationshipId;
import com.kandex.regulation.cms.model.enums.RegulationType;
import com.kandex.regulation.cms.model.mapper.RegulationContentMapper;
import com.kandex.regulation.cms.model.mapper.RegulationMapper;
import com.kandex.regulation.cms.repository.RegulationContentRepository;
import com.kandex.regulation.cms.repository.RegulationLanguageRepository;
import com.kandex.regulation.cms.repository.RegulationRelationshipRepository;
import com.kandex.regulation.cms.repository.RegulationRepository;
import com.kandex.regulation.cms.repository.RegulationTypeRepository;
import com.kandex.regulation.cms.service.RegulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegulationServiceImpl implements RegulationService {
    private final RegulationRepository regulationRepository;
    private final RegulationTypeRepository regulationTypeRepository;
    private final RegulationContentRepository regulationContentRepository;
    private final RegulationRelationshipRepository regulationRelationshipRepository;
    private final RegulationLanguageRepository regulationLanguageRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegulationResponse addRegulation(RegulationRequest request) {
        checkRegulationRequest(request);

        RegulationEntity entity = RegulationMapper.mapRequestToEntity(request);

        if (RegulationType.MAIN.getName().equals(request.getType())) {
            Long nextSort = regulationRepository.getNextSortForMain();
            entity.setSort(nextSort);
        }

        RegulationEntity savedEntity = regulationRepository.save(entity);
        return RegulationMapper.mapEntityToResponseWithContents(savedEntity);
    }

    private void checkRegulationRequest(RegulationRequest request) {
        checkRegulationName(request.getName());
        checkRegulationType(request.getType());

        List<RegulationContentRequest> contentRequests = request.getContents();
        if (contentRequests != null && !contentRequests.isEmpty()) {
            Set<String> languageCodes = contentRequests.stream()
                    .map(RegulationContentRequest::getLang)
                    .collect(Collectors.toSet());

            Set<String> dbLanguageCodes = regulationLanguageRepository.findLanguagesByCodes(languageCodes);
            Set<String> notFoundCodes = languageCodes.stream()
                    .filter(code -> !dbLanguageCodes.contains(code))
                    .collect(Collectors.toSet());
            if (!notFoundCodes.isEmpty()) {
                throw new BaseUncheckedException("Content language code(s): %s does not exist".formatted(notFoundCodes));
            }
        }
    }

    private void checkRegulationName(String name) {
        regulationRepository.findByName(name)
                .ifPresent(e -> {
                    throw new BaseUncheckedException("Another regulation with such name exists. It should be unique");
                });
    }

    private void checkRegulationType(String type) {
        regulationTypeRepository.findById(type)
                .orElseThrow(() -> new BaseUncheckedException("Regulation type %s does not exist".formatted(type)));
    }

    private boolean isNotEqual(Object firstParam, Object secondParam) {
        return firstParam != null && !firstParam.equals(secondParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegulationResponse editRegulation(Long id, RegulationRequest request) {
        RegulationEntity entity = getRegulationEntity(id);

        String requestedName = request.getName();
        if (isNotEqual(requestedName, entity.getName())) {
            checkRegulationName(requestedName);
            entity.setName(requestedName);
        }

        String requestedType = request.getType();
        if (isNotEqual(requestedType, entity.getType())) {
            checkRegulationType(requestedType);

            if (RegulationType.SUB.getName().equals(requestedType)) {
                if (!entity.getChildren().isEmpty()) {
                    throw new BaseUncheckedException("This main regulation type can not be changed to sub, because it has children");
                }
                entity.setSort(null);
            }
            if (RegulationType.MAIN.getName().equals(requestedType)) {
                if (!entity.getParents().isEmpty()) {
                    throw new BaseUncheckedException("This sub regulation type can not be changed to main, because it has parents");
                }
                Long nextSort = regulationRepository.getNextSortForMain();
                entity.setSort(nextSort);
            }

            entity.setType(requestedType);
        }

        Integer requestedTest = request.getTest();
        if (isNotEqual(requestedTest, entity.getTest())) {
            entity.setTest(requestedTest);
        }

        List<RegulationContentRequest> contentRequests = request.getContents();
        if (contentRequests != null && !contentRequests.isEmpty()) {
            Set<RegulationContentEntity> entityContents = entity.getContents();
            Set<String> oldLangSet = entityContents
                    .stream()
                    .map(RegulationContentEntity::getLang)
                    .collect(Collectors.toSet());

            contentRequests.forEach(contentRequest -> {
                if (!oldLangSet.contains(contentRequest.getLang())) {
                    entityContents.add(RegulationContentMapper.mapRequestToEntity(contentRequest));
                } else {
                    entityContents.stream()
                            .filter(e -> e.getLang().equals(contentRequest.getLang()))
                            .findFirst()
                            .ifPresent(e -> {
                                e.setTitle(contentRequest.getTitle());
                                e.setText(contentRequest.getText());
                            });
                }
            });
        }

        return RegulationMapper.mapEntityToResponseWithContents(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMainRegulationSort(Long mainId, Long sort) {
        RegulationEntity entity = getRegulationEntity(mainId, RegulationType.MAIN);
        entity.setSort(sort);
    }

    @Override
    public RegulationsResponse getRegulations(boolean includeChildren) {
        List<RegulationEntity> mainEntities;
        if (includeChildren) {
            mainEntities = regulationRepository.findByTypeJoinFetchRelationships(RegulationType.MAIN.getName());
        } else {
            mainEntities = regulationRepository.findByType(RegulationType.MAIN.getName());
        }
        return RegulationMapper.mapEntitiesToResponse(mainEntities, includeChildren, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRegulation(Long id) {
        RegulationEntity entity = getRegulationEntity(id);
        if (RegulationType.MAIN.getName().equals(entity.getType())) {
            if (!entity.getChildren().isEmpty()) {
                throw new BaseUncheckedException("This main regulation has children so it can not be deleted");
            }
        }
        if (RegulationType.SUB.getName().equals(entity.getType())) {
            if (!entity.getParents().isEmpty()) {
                throw new BaseUncheckedException("This sub-regulation has parent(s) so it can not be deleted");
            }
        }
        regulationRepository.delete(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSubRegulationToMain(Long mainId, Long subId) {
        RegulationEntity mainRegulation = getRegulationEntity(mainId, RegulationType.MAIN);
        RegulationEntity subRegulation = getRegulationEntity(subId, RegulationType.SUB);

        RegulationRelationshipId relationshipId = new RegulationRelationshipId(mainId, subId);
        regulationRelationshipRepository.findById(relationshipId)
                .ifPresentOrElse(e -> {
                            throw new BaseUncheckedException("This sub-regulation is already child of main regulation");
                        },
                        () -> {
                            Long nextSort = regulationRelationshipRepository.getNextSortForParent(mainId);

                            RegulationRelationshipEntity relationship = new RegulationRelationshipEntity();
                            relationship.setId(relationshipId);
                            relationship.setParent(mainRegulation);
                            relationship.setChild(subRegulation);
                            relationship.setSort(nextSort);
                            regulationRelationshipRepository.save(relationship);
                        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSubRegulationFromMain(Long mainId, Long subId) {
        getRegulationEntity(mainId, RegulationType.MAIN);
        getRegulationEntity(subId, RegulationType.SUB);

        RegulationRelationshipId relationshipId = new RegulationRelationshipId(mainId, subId);
        regulationRelationshipRepository.findById(relationshipId)
                .ifPresentOrElse(regulationRelationshipRepository::delete,
                        () -> {
                            throw new BaseUncheckedException("No relationship exists to delete");
                        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRelationshipSort(Long mainId, Long subId, Long sort) {
        getRegulationEntity(mainId, RegulationType.MAIN);
        getRegulationEntity(subId, RegulationType.SUB);

        RegulationRelationshipId relationshipId = new RegulationRelationshipId(mainId, subId);
        regulationRelationshipRepository.findById(relationshipId)
                .ifPresentOrElse(relationship -> relationship.setSort(sort),
                        () -> {
                            throw new BaseUncheckedException("No relationship exists to update sort");
                        });
    }

    @Override
    public RegulationsResponse getSubRegulations(Long mainId) {
        RegulationEntity mainRegulation = getRegulationEntity(mainId);
        Set<RegulationRelationshipEntity> relationships = mainRegulation.getRelationships();
        return RegulationMapper.mapRelationshipEntitiesToResponse(relationships);
    }

    @Override
    public RegulationsResponse getPossibleSubRegulations(Long mainRegulationId) {
        List<RegulationEntity> possibleSubRegulations =
                regulationRepository.findPossibleSubRegulations(mainRegulationId);
        return RegulationMapper.mapEntitiesToResponse(possibleSubRegulations, false, false);
    }

    @Override
    public RegulationContentsResponse getRegulationContents(Long regulationId) {
        RegulationEntity regulationEntity = getRegulationEntity(regulationId);
        Set<RegulationContentEntity> regulationContents =
                regulationEntity.getContents();
        return RegulationContentMapper.mapEntitiesToResponse(regulationContents);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegulationContentResponse addRegulationContent(Long regulationId, RegulationContentRequest request) {
        RegulationEntity regulationEntity = getRegulationEntity(regulationId);

        RegulationContentEntity entity = RegulationContentMapper.mapRequestToEntity(request);
        entity.setRegulation(regulationEntity);
        RegulationContentEntity savedEntity = regulationContentRepository.save(entity);
        return RegulationContentMapper.mapEntityToResponse(savedEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRegulationContent(Long regulationId, Long contentId) {
        RegulationEntity regulationEntity = getRegulationEntity(regulationId);

        regulationContentRepository.findById(contentId)
                .ifPresentOrElse(content -> {
                            if (!content.getRegulation().equals(regulationEntity)) {
                                throw new BaseUncheckedException("Content does not belong to this regulation");
                            }
                            regulationContentRepository.delete(content);
                        },
                        () -> {
                            throw new BaseUncheckedException("Content does not exist");
                        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegulationContentResponse editRegulationContent(
            Long id,
            Long contentId,
            RegulationContentRequest request
    ) {
        RegulationEntity regulationEntity = getRegulationEntity(id);

        RegulationContentEntity contentEntity = regulationContentRepository.findById(contentId)
                .orElseThrow(() -> new BaseUncheckedException("Content does not exist"));

        if (!contentEntity.getRegulation().equals(regulationEntity)) {
            throw new BaseUncheckedException("Content does not belong to this regulation");
        }

        if (request.getLang() != null && !request.getLang().equals(contentEntity.getLang())) {
            contentEntity.setLang(request.getLang());
        }

        if (request.getTitle() != null && !request.getTitle().equals(contentEntity.getTitle())) {
            contentEntity.setTitle(request.getTitle());
        }

        if (request.getText() != null && !request.getText().equals(contentEntity.getText())) {
            contentEntity.setText(request.getText());
        }

        return RegulationContentMapper.mapEntityToResponse(contentEntity);
    }

    private RegulationEntity getRegulationEntity(Long regulationId) {
        return regulationRepository.findById(regulationId)
                .orElseThrow(() ->
                        new BaseUncheckedException("Regulation with id: %s does not exist".formatted(regulationId)));
    }

    private RegulationEntity getRegulationEntity(Long mainId, RegulationType regulationType) {
        return regulationRepository.findByIdAndType(mainId, regulationType.getName())
                .orElseThrow(() -> new BaseUncheckedException(
                        "Regulation with type '%s' not found for such id".formatted(regulationType.getName())));
    }
}
