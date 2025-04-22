package com.kandex.regulation.cms.controller;

import com.kandex.regulation.cms.model.dto.request.RegulationRequest;
import com.kandex.regulation.cms.model.dto.request.RegulationContentRequest;
import com.kandex.regulation.cms.model.dto.response.RegulationResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationContentResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationContentsResponse;
import com.kandex.regulation.cms.model.dto.response.RegulationsResponse;
import com.kandex.regulation.cms.service.RegulationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/regulation-cms/regulations")
@RequiredArgsConstructor
//@SecurityRequirement(name = "Authorization")
public class RegulationController {
    private final RegulationService regulationService;

    @Operation(summary = "Add regulation")
    @PostMapping
    public RegulationResponse addRegulation(@RequestBody @Valid RegulationRequest request) {
        return regulationService.addRegulation(request);
    }

    @Operation(summary = "Get main regulations with/without children")
    @GetMapping
    public RegulationsResponse getRegulations(@RequestParam(required = false) boolean includeChildren) {
        return regulationService.getRegulations(includeChildren);
    }

    @Operation(summary = "Delete regulation")
    @DeleteMapping("/{id}")
    public void deleteRegulation(@PathVariable Long id) {
        regulationService.deleteRegulation(id);
    }

    @Operation(summary = "Update sort for main regulation")
    @PatchMapping("/{mainId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMainRegulationSort(@PathVariable Long mainId, @RequestParam Long sort) {
        regulationService.updateMainRegulationSort(mainId, sort);
    }

    @Operation(summary = "Edit regulation")
    @PutMapping("/{id}")
    public RegulationResponse editRegulation(@PathVariable Long id, @RequestBody RegulationRequest request) {
        return regulationService.editRegulation(id, request);
    }

    @Operation(summary = "Add sub regulation as child of main regulation")
    @PostMapping("/{mainId}/sub-regulations/{subId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addSubRegulationToMain(@PathVariable Long mainId, @PathVariable Long subId) {
        regulationService.addSubRegulationToMain(mainId, subId);
    }

    @Operation(summary = "Delete sub regulation from the children list of main regulation")
    @DeleteMapping("/{mainId}/sub-regulations/{subId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubRegulationFromMain(@PathVariable Long mainId, @PathVariable Long subId) {
        regulationService.deleteSubRegulationFromMain(mainId, subId);
    }

    @Operation(summary = "Update sort of sub-regulation in the context of main regulation")
    @PatchMapping("/{mainId}/sub-regulations/{subId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRelationshipSort(@PathVariable Long mainId, @PathVariable Long subId, @RequestParam Long sort) {
        regulationService.updateRelationshipSort(mainId, subId, sort);
    }

    @Operation(summary = "Get sub-regulations for main regulation")
    @GetMapping("/{mainId}/sub-regulations")
    public RegulationsResponse getSubRegulations(@PathVariable Long mainId) {
        return regulationService.getSubRegulations(mainId);
    }

    @Operation(summary = "Get possible sub-regulations for main regulation")
    @GetMapping("/{mainId}/possible-sub-regulations")
    public RegulationsResponse getPossibleSubRegulations(@PathVariable Long mainId) {
        return regulationService.getPossibleSubRegulations(mainId);
    }

    @Operation(summary = "Get contents for any regulation")
    @GetMapping("/{id}/contents")
    public RegulationContentsResponse getRegulationContents(@PathVariable Long id) {
        return regulationService.getRegulationContents(id);
    }

    @Operation(summary = "Add Content for any regulation")
    @PostMapping("/{id}/contents")
    public RegulationContentResponse addRegulationContent(
            @PathVariable Long id,
            @RequestBody @Valid RegulationContentRequest request
    ) {
        return regulationService.addRegulationContent(id, request);
    }

    @Operation(summary = "Delete content for any regulation")
    @DeleteMapping("/{id}/contents/{contentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegulationContent(
            @PathVariable Long id,
            @PathVariable Long contentId
    ) {
        regulationService.deleteRegulationContent(id, contentId);
    }

    @Operation(summary = "Edit content for any regulation")
    @PutMapping("/{id}/contents/{contentId}")
    public RegulationContentResponse editRegulationContent(
            @PathVariable Long id,
            @PathVariable Long contentId,
            @RequestBody RegulationContentRequest request
    ) {
        return regulationService.editRegulationContent(id, contentId, request);
    }
}
