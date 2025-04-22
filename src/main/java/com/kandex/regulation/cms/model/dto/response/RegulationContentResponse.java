package com.kandex.regulation.cms.model.dto.response;

public record RegulationContentResponse(
        Long id,
        String langCode,
        String title,
        String text
) {
}