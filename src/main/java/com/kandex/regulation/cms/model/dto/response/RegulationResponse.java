package com.kandex.regulation.cms.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegulationResponse {
    private Long id;
    private String name;
    private String type;
    private Long sort;
    private Integer test;
    private LocalDateTime createdAt;
    private List<RegulationContentResponse> contents;
    private List<RegulationResponse> children;
}
