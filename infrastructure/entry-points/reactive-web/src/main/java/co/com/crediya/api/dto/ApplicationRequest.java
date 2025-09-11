package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Required data to register a loan application")
public class ApplicationRequest {
    
    @NotBlank
    @Schema(description = "Identity document number of the applicant", 
            example = "12345678", 
            required = true,
            minLength = 6,
            maxLength = 15)
    private String identityDocument;
    
    @NotNull
@DecimalMin(value = "0.0", inclusive = false, message = "The amount must be greater than 0")
@Digits(integer = 10, fraction = 2, message = "The amount must have maximum 10 integers and 2 decimals")
    @Schema(description = "Requested amount for the loan", 
            example = "100000.50", 
            required = true,
            minimum = "0.01",
            pattern = "^\\d{1,10}(\\.\\d{1,2})?$")
    private BigDecimal amount;
    
    @NotNull
    @Schema(description = "Term in months for the loan", 
            example = "12", 
            required = true,
            minimum = "1",
            maximum = "60")
    private Integer termMonths;
    
    @NotNull
    @Schema(description = "ID of the requested loan type", 
            example = "1", 
            required = true,
            minimum = "1")
    private Integer typeId;
}