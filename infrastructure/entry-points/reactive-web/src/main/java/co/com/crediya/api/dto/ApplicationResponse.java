package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response for loan application registration")
public class ApplicationResponse {

    @Schema(description = "Operation response code", 
            example = "0", 
            allowableValues = {"0", "1"})
    private Integer code;
    
    @Schema(description = "Descriptive message of the operation result", 
            example = "Pending review")
    private String message;
    
    @Schema(description = "Trace ID for error tracking", 
            example = "550e8400-e29b-41d4-a716-446655440000")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String traceId;

    public static ApplicationResponse success(String message) {
        return new ApplicationResponse(0, message, null);
    }

    public static ApplicationResponse error(String message) {
        return new ApplicationResponse(1, message, null);
    }
    
    public static ApplicationResponse error(String message, String traceId) {
        return new ApplicationResponse(1, message, traceId);
    }

    public static ApplicationResponse internalError(String message, String traceId) {
        return new ApplicationResponse(1, message, traceId);
    }
}