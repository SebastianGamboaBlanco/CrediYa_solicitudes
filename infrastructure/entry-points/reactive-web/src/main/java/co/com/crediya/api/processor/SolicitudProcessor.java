package co.com.crediya.api.processor;

import co.com.crediya.api.dto.RegistrarSolicitudRequest;
import co.com.crediya.api.helper.ValidationUtil;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SolicitudProcessor {

    private final RegistrarSolicitudUseCase registrarSolicitudUseCase;
    private final ValidationUtil validationUtils;

    public Mono<String> procesarSolicitud(RegistrarSolicitudRequest request) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "");
            MDC.put("traceId", traceId);
            MDC.put("operation", "registro");

            log.info("Procesando solicitud: doc={}, monto={}, plazo={}, tipoPrestamo={}",
                    request.getDocumentoIdentidad(), request.getMonto(), request.getPlazo(), request.getIdTipoPrestamo());

            return validationUtils.validateRequest(request)
                    .doOnNext(req -> {
                        log.debug("Validaciones Bean Validation completadas");
                    })
                .flatMap(validatedRequest -> registrarSolicitudUseCase.registrar(
                        validatedRequest.getDocumentoIdentidad(),
                        validatedRequest.getMonto(),
                        validatedRequest.getPlazo(),
                        validatedRequest.getIdTipoPrestamo()
                ))
                .doOnNext(resultado -> log.info("Solicitud procesada exitosamente: {}", resultado))
                .doOnError(error -> {
                     MDC.put("traceId", traceId);
                     log.error("FIN CON ERROR - Error procesando consulta - DocumentoID: {}, Tipo: {}, Mensaje: {}",
                      request.getDocumentoIdentidad(), error.getClass().getSimpleName(), error.getMessage());
                });
        });
    }

}