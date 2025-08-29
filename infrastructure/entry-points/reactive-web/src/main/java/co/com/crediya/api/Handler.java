package co.com.crediya.api;

import co.com.crediya.api.dto.RegistrarSolicitudRequest;
import co.com.crediya.api.dto.RegistrarSolicitudResponse;
import co.com.crediya.api.dto.SolicitudDTOMapper;
import co.com.crediya.api.exception.ErrorHandler;
import co.com.crediya.api.processor.SolicitudProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    
    private final SolicitudProcessor solicitudProcessor;
    private final ErrorHandler errorHandler;

    public Mono<ServerResponse> registrarSolicitud(ServerRequest serverRequest) {
        log.info("=== INICIANDO REGISTRO DE SOLICITUD ===");
        
        return serverRequest.bodyToMono(RegistrarSolicitudRequest.class)
                .doOnNext(request -> log.info("Request recibido: doc={}, monto={}, plazo={}, tipoPrestamo={}", 
                        request.getDocumentoIdentidad(), request.getMonto(), request.getPlazo(), request.getIdTipoPrestamo()))
                .flatMap(solicitudProcessor::procesarSolicitud)
                .flatMap(response -> {
                    RegistrarSolicitudResponse resul = SolicitudDTOMapper.toResponse(200, response);
                    HttpStatus status = HttpStatus.OK;
                    return ServerResponse.status(status)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(resul);

                })
                .onErrorResume(errorHandler::handleError);

    }
}
