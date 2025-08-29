package co.com.crediya.api.controller;

import co.com.crediya.api.dto.RegistrarSolicitudRequest;
import co.com.crediya.api.dto.RegistrarSolicitudResponse;
import co.com.crediya.api.processor.SolicitudProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Solicitudes", description = "API para gestión de solicitudes de préstamos")
public class SolicitudController {

    private final SolicitudProcessor solicitudProcessor;

    @Operation(
            summary = "Registrar solicitud de préstamo",
            description = "Permite registrar una nueva solicitud de préstamo validando la existencia del usuario y el tipo de préstamo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitud registrada exitosamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegistrarSolicitudResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en los datos enviados o validación de negocio",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegistrarSolicitudResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegistrarSolicitudResponse.class)
                    )
            )
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<RegistrarSolicitudResponse> registrarSolicitud(
            @Parameter(description = "Datos de la solicitud de préstamo", required = true)
            @RequestBody @Validated RegistrarSolicitudRequest request) {
        
        log.info("=== INICIANDO REGISTRO DE SOLICITUD ===");
        log.info("Request recibido: doc={}, monto={}, plazo={}, tipoPrestamo={}", 
                request.getDocumentoIdentidad(), request.getMonto(), request.getPlazo(), request.getIdTipoPrestamo());
        
        return solicitudProcessor.procesarSolicitud(request)
                .map(resultado -> RegistrarSolicitudResponse.builder()
                        .status(HttpStatus.OK.value())
                        .mensaje(resultado)
                        .build())
                .doOnNext(response -> log.info("Respuesta enviada: {}", response));
    }
}