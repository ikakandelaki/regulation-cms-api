package com.kandex.regulation.cms.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralExceptionResponse {
    private Long statusCode;
    private String statusMessage;
    private String additionalMessage;
    private final boolean success = false;
}
