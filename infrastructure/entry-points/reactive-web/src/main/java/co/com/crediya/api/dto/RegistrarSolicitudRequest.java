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
@Schema(description = "Datos requeridos para registrar una solicitud de préstamo")
public class RegistrarSolicitudRequest {
    
    @NotBlank
    @Schema(description = "Número de documento de identidad del solicitante", 
            example = "12345678", 
            required = true,
            minLength = 6,
            maxLength = 15)
    private String documentoIdentidad;
    
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor que 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 enteros y 2 decimales")
    @Schema(description = "Monto solicitado para el préstamo", 
            example = "100000.50", 
            required = true,
            minimum = "0.01",
            pattern = "^\\d{1,10}(\\.\\d{1,2})?$")
    private BigDecimal monto;
    
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "El plazo debe ser mayor que 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 enteros y 2 decimales")
    @Schema(description = "Plazo en meses para el préstamo", 
            example = "12", 
            required = true,
            minimum = "1",
            maximum = "60")
    private Integer plazo;
    
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "El tipo de prestamo debe ser mayor que 0")
    @Schema(description = "ID del tipo de préstamo solicitado", 
            example = "1", 
            required = true,
            minimum = "1")
    private Integer idTipoPrestamo;
}