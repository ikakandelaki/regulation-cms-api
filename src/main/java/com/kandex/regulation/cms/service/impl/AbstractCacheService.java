package com.kandex.regulation.cms.service.impl;

import com.kandex.regulation.cms.model.dto.response.RegulationResponse;
import com.kandex.regulation.cms.model.entity.RegulationContentEntity;
import com.kandex.regulation.cms.model.entity.RegulationRelationshipEntity;
import com.kandex.regulation.cms.model.enums.RegulationType;
import com.kandex.regulation.cms.model.mapper.RegulationMapper;
import com.kandex.regulation.cms.repository.RegulationRepository;
import com.kandex.regulation.cms.service.CacheService;
import com.kandex.regulation.cms.util.CacheConstant;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
abstract class AbstractCacheService implements CacheService {
    @PostConstruct
    public void populateCaches() {
        try {
            checkIfCacheIsRunning();
            clearCache();
            populateRegulationCaches();
        } catch (Exception e) {
            log.error("Error during cache population", e);
        }
    }

    @SuppressWarnings("all")
    abstract <T> void saveInHash(String key, Map<String, T> map);

    abstract void checkIfCacheIsRunning();

    abstract void clearCache();

    abstract RegulationRepository getRegulationRepository();


    private record ChildIdAndSort(String id, Long sort) {

    }

    private record RegIdAndText(String id, String text) {

    }

    private void populateRegulationCaches() {
        Map<String, RegulationResponse> mainRegulationsMap = new HashMap<>();
        Map<String, RegulationResponse> subRegulationsMap = new HashMap<>();
        Map<String, RegIdAndText> regulationTitlesMap = new HashMap<>();
        Map<String, RegIdAndText> regulationTextsMap = new HashMap<>();
        Map<String, Set<ChildIdAndSort>> relationshipsMap = new HashMap<>();

        getRegulationRepository().findAllJoinFetch()
                .forEach(regulationEntity -> {
                    RegulationResponse regulationResponse = RegulationMapper.mapEntityToResponse(regulationEntity);
                    String regulationIdString = regulationResponse.getId().toString();

                    // todo: maybe check on test field and if it's 1, do not cache
                    if (RegulationType.MAIN.getName().equals(regulationResponse.getType())) {
                        mainRegulationsMap.put(regulationIdString, regulationResponse);
                    } else {
                        subRegulationsMap.put(regulationIdString, regulationResponse);
                    }

                    Set<RegulationContentEntity> contentEntities = regulationEntity.getContents();
                    contentEntities.forEach(contentEntity -> {
                        String lang = contentEntity.getLang();
                        String title = contentEntity.getTitle();
                        String text = contentEntity.getText();
                        String regulationIdAndLang = regulationIdString + "_" + lang;
                        regulationTitlesMap.put(regulationIdAndLang, new RegIdAndText(regulationIdString, title));
                        regulationTextsMap.put(regulationIdAndLang, new RegIdAndText(regulationIdString, text));
                    });

                    Set<RegulationRelationshipEntity> relationshipEntities = regulationEntity.getRelationships();
                    relationshipEntities.forEach(relationshipEntity -> {
                        String childIdString = relationshipEntity.getChild().getId().toString();
                        addValueInSetOfMap(regulationIdString,
                                new ChildIdAndSort(childIdString, relationshipEntity.getSort()),
                                relationshipsMap);
                    });
                });

        saveInHash(CacheConstant.MAIN_REGULATION_KEY, mainRegulationsMap);
        saveInHash(CacheConstant.SUB_REGULATION_KEY, subRegulationsMap);
        saveInHash(CacheConstant.REGULATION_RELATIONSHIP_KEY, relationshipsMap);
        saveInHash(CacheConstant.REGULATION_TITLE_KEY, regulationTitlesMap);
        saveInHash(CacheConstant.REGULATION_TEXT_KEY, regulationTextsMap);
    }

    private <T> void addValueInSetOfMap(String key, T value, Map<String, Set<T>> map) {
        map.putIfAbsent(key, new HashSet<>());
        map.get(key).add(value);
    }
}
