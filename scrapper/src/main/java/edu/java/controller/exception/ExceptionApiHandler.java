package edu.java.controller.exception;

import edu.java.controller.dto.response.ApiErrorResponse;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    private static final String API_ERROR_RESPONSE_DESCRIPTION = "Internal Server Error";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> runtimeException(RuntimeException exception) {
        return ResponseEntity
            .badRequest()
            .body(createApiErrorResponse(API_ERROR_RESPONSE_DESCRIPTION, exception));
    }

    private ApiErrorResponse createApiErrorResponse(String description, Exception exception) {
        return new ApiErrorResponse(
            description,
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            exception.getMessage(),
            Stream.of(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }
}
