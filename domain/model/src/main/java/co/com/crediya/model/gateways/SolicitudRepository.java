package co.com.crediya.model.gateways;

import co.com.crediya.model.Solicitud;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {
    Mono<Void> registrarSolicitud(Solicitud solicitud);
    Mono<Boolean> existeIdTipoPrestamo(Integer idTipoPrestamo);
}
