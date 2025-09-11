package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Validation error detail for a specific field")
public class FieldError {

    @Schema(description = "Name of the field containing the error", example = "identityDocument")
    private String field;

    @Schema(description = "Descriptive validation error message", 
            example = "The identity document cannot be empty")
    private String message;

    @Schema(description = "Value that was rejected by validation", example = "")
    private Object rejectedValue;
}
