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
        log.debug("Starting Bean Validation for object: {}", object.getClass().getSimpleName());
        
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        
        if (!violations.isEmpty()) {
            List<Tuple3<String, String, String>> errors = violations.stream()
                .filter(violation -> violation != null)
                .map(violation -> {
                    try {
                        String field = Objects.isNull(violation.getPropertyPath()) ? "unknown" : violation.getPropertyPath().toString();
                        String message = Objects.isNull(violation.getMessage()) ? "Validation error" : violation.getMessage();
                        String value = Objects.isNull(violation.getInvalidValue()) ? "" : violation.getInvalidValue().toString();
                        
                        log.debug("Creating tuple - Field: '{}', Message: '{}', Value: '{}'", field, message, value);
                        return Tuples.of(field, message, value);
                    } catch (Exception e) {
                        log.error("Error processing violation: {}", e.getMessage(), e);
                        return Tuples.of("unknown", "Validation error", "");
                    }
                })
                .collect(Collectors.toList());
            
            String errorMessage = errors.stream()
                .map(tuple -> tuple.getT1() + ": " + tuple.getT2())
                .collect(Collectors.joining(", "));
            
            log.debug("Validation errors found: {}", errorMessage);
            return Mono.error(new MultipleValidationException(errors));
        }
        
        log.debug("Successful validation for object: {}", object.getClass().getSimpleName());
        return Mono.just(object);
    }
}