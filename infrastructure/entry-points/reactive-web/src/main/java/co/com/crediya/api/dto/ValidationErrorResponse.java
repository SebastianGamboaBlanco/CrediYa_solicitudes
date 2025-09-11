package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.util.function.Tuple3;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Validation error response with specific details per field")
public class ValidationErrorResponse {

    @Schema(description = "Response code", example = "1")
    private Integer code;

    @Schema(description = "List of validation errors per field")
    private List<FieldError> errors;

    public static ValidationErrorResponse from(List<Tuple3<String, String, String>> validationErrors) {
        List<FieldError> fieldErrors = validationErrors.stream()
                .filter(tuple -> tuple != null)
                .map(tuple -> new FieldError(
                        tuple.getT1(),
                        tuple.getT2(),
                        tuple.getT3() != null && !tuple.getT3().equals("") ? tuple.getT3() : null
                ))
                .collect(Collectors.toList());

        return new ValidationErrorResponse(1, fieldErrors);
    }
}
