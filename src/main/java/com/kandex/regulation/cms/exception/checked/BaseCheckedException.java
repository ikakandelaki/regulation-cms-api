package com.kandex.regulation.cms.exception.checked;

import com.kandex.regulation.cms.model.enums.StatusCode;
import lombok.Getter;

@Getter
public class BaseCheckedException extends Exception {

    protected final StatusCode statusCode;
    protected final String message;

    public BaseCheckedException(StatusCode statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
