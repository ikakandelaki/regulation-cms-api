package com.kandex.regulation.cms.model.dto.response;

import java.util.List;

public record RegulationsResponse(
        List<RegulationResponse> regulations
) {
}
