package co.com.crediya.api.exception;

import co.com.crediya.api.dto.AuthErrorResponse;
import co.com.crediya.api.dto.FieldError;

import co.com.crediya.api.dto.ApplicationResponse;
import co.com.crediya.api.dto.ValidationErrorResponse;

import co.com.crediya.model.exceptions.MultipleValidationException;
import co.com.crediya.model.exceptions.LoanApplicationException;
import co.com.crediya.model.exceptions.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class ErrorHandler {

    private final ObjectMapper objectMapper;

    public ErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Mono<ServerResponse> handleError(Throwable error) {
        String traceId = MDC.get("X-FLOW-ID");
        if (traceId == null) {
            traceId = "unknown";
        }

        String errorClassName = error.getClass().getSimpleName();
        String errorMessage = error.getMessage();

        log.error("[{}] Error process request - Type: {}, Message: {}", traceId, errorClassName, errorMessage, error);

        return handleErrorWithTraceId(error, traceId, errorMessage);
    }

    private Mono<ServerResponse> handleErrorWithTraceId(Throwable error, String traceId, String errorMessage) {


        if (error instanceof MultipleValidationException multipleValidationException) {
            log.warn("[{}] Multiple validation errors - Count: {}", traceId, multipleValidationException.getErrors().size());
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

            log.warn("[{}] Missing required field - Field: {}", traceId, fieldName);
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(response);
        }

        if (error instanceof LoanApplicationException loanApplicationException) {
            ErrorType errorType = loanApplicationException.getErrorType();

            if (errorType == ErrorType.FORBIDDEN) {
                log.warn("[{}] Access denied - {}", traceId, errorMessage);
                return ServerResponse.status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApplicationResponse.error(errorMessage, traceId));
            }

            if (errorType == ErrorType.UNAUTHORIZED) {
                log.warn("[{}] Unauthorized access - {}", traceId, errorMessage);
                return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApplicationResponse.error(errorMessage, traceId));
            }

            // Otros tipos de error de LoanApplicationException
            log.warn("[{}] Business logic error - Type: {}, Message: {}", traceId, errorType, errorMessage);
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ApplicationResponse.error(errorMessage, traceId));
        }

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ApplicationResponse.internalError("Internal server error", traceId));

    }

    private AuthErrorResponse buildAuthErrorResponse(Throwable error) {
        String errorMessage = error.getMessage();

        if (errorMessage != null && errorMessage.contains("JWT token not found")) {
            return AuthErrorResponse.tokenRequired();
        } else if (errorMessage != null && errorMessage.contains("invalid")) {
            return AuthErrorResponse.tokenInvalid();
        } else {
            return AuthErrorResponse.unauthorized("Authentication error");
        }
    }

    public Mono<Void> handleJwtAuthenticationError(ServerWebExchange exchange, Throwable error) {
        log.error("JWT AUTHENTICATION ERROR - CENTRALIZED HANDLER: {}", error.getMessage());

        AuthErrorResponse authResponse = buildAuthErrorResponse(error);

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        try {
            String responseBody = objectMapper.writeValueAsString(authResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBody.getBytes());
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Error serializing authentication response", e);
            return exchange.getResponse().setComplete();
        }
    }

}


