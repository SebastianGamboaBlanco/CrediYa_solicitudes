package co.com.crediya.api.dto;

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

    public static ApplicationResponse success(String message) {
        return new ApplicationResponse(0, message);
    }

    public static ApplicationResponse error(String message) {
        return new ApplicationResponse(1, message);
    }

    public static ApplicationResponse internalError(String message) {
        return new ApplicationResponse(1, message);
    }
}