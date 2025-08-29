package co.com.crediya.api.helper;

import co.com.crediya.model.exceptions.MultipleValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationUtil {
    
    private final Validator validator;
    
    public <T> Mono<T> validateRequest(T object) {
        log.debug("Iniciando validación Bean Validation para objeto: {}", object.getClass().getSimpleName());
        
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        
        if (!violations.isEmpty()) {
            List<Tuple3<String, String, String>> errors = violations.stream()
                .filter(violation -> violation != null)
                .map(violation -> {
                    try {
                        String campo = Objects.isNull(violation.getPropertyPath()) ? "unknown" : violation.getPropertyPath().toString();
                        String mensaje = Objects.isNull(violation.getMessage()) ? "Error de validación" : violation.getMessage();
                        String valor = Objects.isNull(violation.getInvalidValue()) ? "" : violation.getInvalidValue().toString();
                        
                        log.debug("Creando tupla - Campo: '{}', Mensaje: '{}', Valor: '{}'", campo, mensaje, valor);
                        return Tuples.of(campo, mensaje, valor);
                    } catch (Exception e) {
                        log.error("Error procesando violación: {}", e.getMessage(), e);
                        return Tuples.of("unknown", "Error de validación", "");
                    }
                })
                .collect(Collectors.toList());
            
            String errorMessage = errors.stream()
                .map(tuple -> tuple.getT1() + ": " + tuple.getT2())
                .collect(Collectors.joining(", "));
            
            log.debug("Errores de validación encontrados: {}", errorMessage);
            return Mono.error(new MultipleValidationException(errors));
        }
        
        log.debug("Validación exitosa para objeto: {}", object.getClass().getSimpleName());
        return Mono.just(object);
    }
}