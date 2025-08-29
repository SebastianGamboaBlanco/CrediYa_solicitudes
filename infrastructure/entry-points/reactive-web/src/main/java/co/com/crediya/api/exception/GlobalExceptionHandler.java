package co.com.crediya.api.exception;

import co.com.crediya.api.dto.RegistrarSolicitudResponse;
import co.com.crediya.model.exceptions.MultipleValidationException;
import co.com.crediya.model.exceptions.SolicitudException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SolicitudException.class)
    public Mono<ResponseEntity<RegistrarSolicitudResponse>> handleSolicitudException(SolicitudException ex) {
        log.error("Error de solicitud: {}", ex.getMessage(), ex);
        RegistrarSolicitudResponse response = RegistrarSolicitudResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensaje(ex.getMessage())
                .build();
        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    @ExceptionHandler(MultipleValidationException.class)
    public Mono<ResponseEntity<RegistrarSolicitudResponse>> handleMultipleValidationException(MultipleValidationException ex) {
        log.error("Errores de validación: {}", ex.getErrors(), ex);
        String mensaje = ex.getErrors().stream()
                .map(tuple -> tuple.getT1() + ": " + tuple.getT2())
                .collect(Collectors.joining(", "));
        
        RegistrarSolicitudResponse response = RegistrarSolicitudResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensaje(mensaje)
                .build();
        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<RegistrarSolicitudResponse>> handleValidationException(WebExchangeBindException ex) {
        log.error("Error de validación de campos: {}", ex.getMessage(), ex);
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        RegistrarSolicitudResponse response = RegistrarSolicitudResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensaje(mensaje)
                .build();
        return Mono.just(ResponseEntity.badRequest().body(response));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<RegistrarSolicitudResponse>> handleGenericException(Exception ex) {
        log.error("Error interno: {}", ex.getMessage(), ex);
        RegistrarSolicitudResponse response = RegistrarSolicitudResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .mensaje("Error interno del servidor")
                .build();
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
    }
}