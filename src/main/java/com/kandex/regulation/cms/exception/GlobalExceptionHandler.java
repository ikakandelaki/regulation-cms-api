package com.kandex.regulation.cms.exception;

import com.kandex.regulation.cms.exception.unchecked.BaseUncheckedException;
import com.kandex.regulation.cms.model.dto.response.GeneralExceptionResponse;
import com.kandex.regulation.cms.model.enums.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxAllowedFileSize;

    @ExceptionHandler(BaseUncheckedException.class)
    public ResponseEntity<Object> handleBaseUncheckedException(BaseUncheckedException exception) {
        logError(exception);

        StatusCode statusCode = StatusCode.INTERNAL_ERROR;
        if (exception.getStatusCode() != null) {
            statusCode = exception.getStatusCode();
        }
        String message = exception.getMessage();

        GeneralExceptionResponse response = new GeneralExceptionResponse(statusCode.getCode(), statusCode.name(), message);
        return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        logError(exception);

        StatusCode statusCode = StatusCode.BAD_REQUEST;
        String errorMessage = "'%s' is illegal argument for parameter '%s'".formatted(exception.getValue(), exception.getName());

        GeneralExceptionResponse response = new GeneralExceptionResponse(statusCode.getCode(), statusCode.name(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<GeneralExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        logError(exception);

        StatusCode statusCode = StatusCode.HTTP_MESSAGE_NOT_READABLE;
        String message = "Http message not readable";
        GeneralExceptionResponse response = new GeneralExceptionResponse(statusCode.getCode(), statusCode.name(), message);
        return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<GeneralExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logError(exception);

        StatusCode statusCode = StatusCode.BAD_REQUEST;

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> errorMessages = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        // Join the individual error messages into a single string if needed
        String combinedErrorMessage = String.join("; ", errorMessages);

        GeneralExceptionResponse response = new GeneralExceptionResponse(statusCode.getCode(), statusCode.name(), combinedErrorMessage);
        return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralExceptionResponse> handleParseException(Exception exception) {
        logError(exception);

        StatusCode statusCode = StatusCode.INTERNAL_ERROR;
        String message = exception.getMessage();
        GeneralExceptionResponse response = new GeneralExceptionResponse(statusCode.getCode(), statusCode.name(), message);
        return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<GeneralExceptionResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        if (exception.getCause().getCause() instanceof FileSizeLimitExceededException cause) {
            String fileName = cause.getFileName();

            StatusCode statusCode = StatusCode.INTERNAL_ERROR;
            GeneralExceptionResponse response = new GeneralExceptionResponse(
                    statusCode.getCode(),
                    statusCode.name(),
                    "File with name '" + fileName + "' exceeded max allowed size " + maxAllowedFileSize
            );
            return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
        }
        return handleParseException(exception);
    }

    private void logError(Exception ex) {
        log.error("Exception of class: {} throw, with message: {}", Exception.class.getSimpleName(), ex.getMessage());
        log.error("Stacktrace: ", ex);
    }

}
