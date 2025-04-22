package com.kandex.regulation.cms.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegulationLanguageRequest {
    @NotEmpty(message = "Language code must be provided")
    private String code;

    @NotEmpty(message = "Language name must be provided")
    private String name;
}
