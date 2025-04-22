package com.kandex.regulation.cms.model.enums;

import lombok.Getter;

@Getter
public enum StatusCode {
    // 1xxx - 9xxx - common errors
    HTTP_MESSAGE_NOT_READABLE(1000L),
    BAD_REQUEST(1001L),
    INTERNAL_ERROR(1002L);

    // 10xxx - 19xxx - others

    private final Long code;

    StatusCode(Long code) {
        this.code = code;
    }

}
