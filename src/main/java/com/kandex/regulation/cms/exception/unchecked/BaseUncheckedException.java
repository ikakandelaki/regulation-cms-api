package com.kandex.regulation.cms.exception.unchecked;

import com.kandex.regulation.cms.exception.checked.BaseCheckedException;
import com.kandex.regulation.cms.model.enums.StatusCode;
import lombok.Getter;

@Getter
public class BaseUncheckedException extends RuntimeException {

    private final StatusCode statusCode;
    private final String message;

    public BaseUncheckedException(BaseCheckedException ex) {
        this.statusCode = ex.getStatusCode();
        this.message = ex.getMessage();
    }

    public BaseUncheckedException(StatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public BaseUncheckedException(String message) {
        this.statusCode = null;
        this.message = message;
    }
}
