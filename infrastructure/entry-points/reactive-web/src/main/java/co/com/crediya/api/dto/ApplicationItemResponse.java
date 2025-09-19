package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Application item response")
public class ApplicationItemResponse {

    @Schema(description = "Application ID", example = "123")
    private Integer idSolicitud;

    @Schema(description = "Full name of the applicant", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Email address", example = "juan.perez@example.com")
    private String email;

    @Schema(description = "Base salary", example = "3000000.00")
    private BigDecimal salarioBase;

    @Schema(description = "Loan amount", example = "10000000.00")
    private BigDecimal monto;

    @Schema(description = "Term in months", example = "36")
    private Integer plazo;

    @Schema(description = "Interest rate", example = "15.5")
    private BigDecimal tasaInteres;

    @Schema(description = "Loan type ID", example = "1")
    private Integer idTipoPrestamo;

    @Schema(description = "Loan type", example = "Hipotecario")
    private String tipoPrestamo;

    @Schema(description = "Status ID", example = "1")
    private Integer idEstado;

    @Schema(description = "Application status", example = "En revisión")
    private String estadoSolicitud;

    @Schema(description = "Monthly debt amount", example = "350000.00")
    private BigDecimal deudaTotalMensual;
}