package com.kandex.regulation.cms.service;

import com.kandex.regulation.cms.model.dto.request.RegulationLanguageRequest;
import com.kandex.regulation.cms.model.dto.response.RegulationLanguageResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationLanguagesResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationTypesResponse;

public interface RegulationMetadataService {
    RegulationTypesResponse getRegulationTypes();

    RegulationLanguagesResponse getRegulationLanguages();

    RegulationLanguageResponse addRegulationLanguage(RegulationLanguageRequest request);

    void deleteRegulationLanguage(String code);

    RegulationLanguageResponse editRegulationLanguageName(String code, String name);
}
