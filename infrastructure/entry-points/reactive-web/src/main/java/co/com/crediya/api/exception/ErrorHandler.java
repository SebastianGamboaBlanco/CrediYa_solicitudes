package co.com.crediya.api.exception;

import co.com.crediya.api.dto.FieldError;

import co.com.crediya.api.dto.ApplicationResponse;
import co.com.crediya.api.dto.ValidationErrorResponse;

import co.com.crediya.model.exceptions.MultipleValidationException;
import co.com.crediya.model.exceptions.LoanApplicationException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class ErrorHandler {

    public Mono<ServerResponse> handleError(Throwable error) {
        String errorType = error.getClass().getSimpleName();
        String errorMessage = error.getMessage();

        log.error("Error process request - Type: {}, Message: {}", errorType, errorMessage, error);


        if (error instanceof MultipleValidationException multipleValidationException) {
            log.warn("Multiple validation errors - Count: {}", multipleValidationException.getErrors().size());
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ValidationErrorResponse.from(multipleValidationException.getErrors()));
        }

        if (error instanceof MismatchedInputException mismatchedInputException) {
            String fieldName = mismatchedInputException.getPath().isEmpty() ? "unknown" :
                    mismatchedInputException.getPath().get(0).getFieldName();
            String message = "Required field not provided: " + fieldName;

            List<FieldError> fieldErrors = List.of(new FieldError(fieldName, message, null));
            ValidationErrorResponse response = new ValidationErrorResponse(1, fieldErrors);

            log.warn("Missing required field - Field: {}", fieldName);
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(response);
        }

        if (error instanceof LoanApplicationException loanApplicationException){
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ApplicationResponse.error(loanApplicationException.getMessage()));
        }

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ApplicationResponse.internalError("Internal server error"));

    }
}


