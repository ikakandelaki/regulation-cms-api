package com.kandex.regulation.cms.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegulationContentRequest {
    @NotEmpty(message = "Content lang must be provided")
    @Schema(description = "Language code", example = "en")
    private String lang;

    @NotEmpty(message = "Content title must be provided")
    @Schema(description = "Title for provided language", example = "Privacy Policy")
    private String title;

    @Schema(description = "Text for provided language", example = "<p>This is the English version of the policy.</p>")
    private String text;
}
