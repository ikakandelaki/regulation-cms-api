package com.kandex.regulation.cms.controller;

import com.kandex.regulation.cms.model.dto.request.RegulationLanguageRequest;
import com.kandex.regulation.cms.model.dto.response.RegulationLanguageResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationLanguagesResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationTypesResponse;
import com.kandex.regulation.cms.service.RegulationMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regulation-cms/metadata")
@RequiredArgsConstructor
//@SecurityRequirement(name = "Authorization")
public class RegulationMetadataController {
    private final RegulationMetadataService regulationMetadataService;

    @Operation(summary = "Get available languages")
    @GetMapping("/languages")
    public RegulationLanguagesResponse getRegulationLanguages() {
        return regulationMetadataService.getRegulationLanguages();
    }

    @Operation(summary = "Add language")
    @PostMapping("/languages")
    public RegulationLanguageResponse addRegulationLanguage(@RequestBody @Valid RegulationLanguageRequest request) {
        return regulationMetadataService.addRegulationLanguage(request);
    }

    @Operation(summary = "Delete language")
    @DeleteMapping("/languages/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegulationLanguage(@PathVariable String code) {
        regulationMetadataService.deleteRegulationLanguage(code);
    }

    @Operation(summary = "Update language name")
    @PatchMapping("/languages/{code}")
    public RegulationLanguageResponse editRegulationLanguageName(
            @PathVariable String code,
            @RequestParam String name
    ) {
        return regulationMetadataService.editRegulationLanguageName(code, name);
    }

    @Operation(summary = "Get available types of regulations")
    @GetMapping("/types")
    public RegulationTypesResponse getRegulationTypes() {
        return regulationMetadataService.getRegulationTypes();
    }
}
