package com.kandex.regulation.cms.service;

import com.kandex.regulation.cms.model.dto.request.RegulationRequest;
import com.kandex.regulation.cms.model.dto.request.RegulationContentRequest;
import com.kandex.regulation.cms.model.dto.response.RegulationResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationContentResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationContentsResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationsResponse;

public interface RegulationService {
    RegulationResponse addRegulation(RegulationRequest request);

    RegulationsResponse getRegulations(boolean includeChildren);

    void deleteRegulation(Long id);

    RegulationResponse editRegulation(Long id, RegulationRequest request);

    void updateMainRegulationSort(Long mainId, Long sort);

    void addSubRegulationToMain(Long mainId, Long subId);

    void deleteSubRegulationFromMain(Long mainId, Long subId);

    void updateRelationshipSort(Long mainId, Long subId, Long sort);

    RegulationsResponse getSubRegulations(Long mainId);

    RegulationsResponse getPossibleSubRegulations(Long mainRegulationId);

    RegulationContentsResponse getRegulationContents(Long regulationId);

    RegulationContentResponse addRegulationContent(Long regulationId, RegulationContentRequest request);

    void deleteRegulationContent(Long regulationId, Long contentId);

    RegulationContentResponse editRegulationContent(Long id, Long contentId, RegulationContentRequest request);
}
