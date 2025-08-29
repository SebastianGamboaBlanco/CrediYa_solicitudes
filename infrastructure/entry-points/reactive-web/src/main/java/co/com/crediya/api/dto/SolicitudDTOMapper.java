package co.com.crediya.api.dto;

public class SolicitudDTOMapper {
    
    public static RegistrarSolicitudResponse toResponse(Integer status, String mensaje) {
        return RegistrarSolicitudResponse.builder()
                .status(status)
                .mensaje(mensaje)
                .build();
    }
}