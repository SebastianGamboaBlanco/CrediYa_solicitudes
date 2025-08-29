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
@Schema(description = "Respuesta del registro de solicitud de préstamo")
public class RegistrarSolicitudResponse {

    @Schema(description = "Código de estado HTTP de la operación", 
            example = "200", 
            allowableValues = {"200", "400", "500"})
    private Integer status;
    
    @Schema(description = "Mensaje descriptivo del resultado de la operación", 
            example = "Pendiente de revisión")
    private String mensaje;

    public static RegistrarSolicitudResponse internalError(String mensaje) {
        return new RegistrarSolicitudResponse(500, mensaje);
    }

    public static RegistrarSolicitudResponse error(String mensaje) {
        return new RegistrarSolicitudResponse(400, mensaje);
    }
}