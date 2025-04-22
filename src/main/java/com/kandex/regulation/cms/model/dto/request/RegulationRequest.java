package com.kandex.regulation.cms.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegulationRequest {
    @NotEmpty(message = "Name must be provided")
    @Schema(description = "Regulation name/identifier", example = "Data Privacy Regulation")
    private String name;

    @NotEmpty(message = "Type must be provided")
    @Schema(description = "Regulation type (main or sub)", example = "main")
    private String type;

    @Min(value = 0, message = "Test must be 0 or 1")
    @Max(value = 1, message = "Test must be 0 or 1")
    @Schema(description = "Test flag (0 - disable on website, or 1 - enable)", example = "1")
    private Integer test;

    @Valid
    @Schema(description = "Contents for different languages")
    private List<RegulationContentRequest> contents;
}