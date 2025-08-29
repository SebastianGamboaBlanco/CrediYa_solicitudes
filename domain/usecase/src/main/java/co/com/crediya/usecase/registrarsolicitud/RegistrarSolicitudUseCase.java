package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.Solicitud;
import co.com.crediya.model.exceptions.SolicitudException;
import co.com.crediya.model.exceptions.TipoError;
import co.com.crediya.model.gateways.SolicitudRepository;
import co.com.crediya.model.gateways.UsuarioService;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class RegistrarSolicitudUseCase {
    private final UsuarioService usuarioService;
    private final SolicitudRepository solicitudRepository;
    
    public RegistrarSolicitudUseCase(UsuarioService usuarioService, SolicitudRepository solicitudRepository) {
        this.usuarioService = usuarioService;
        this.solicitudRepository = solicitudRepository;
    }
    
    public Mono<String> registrar(String documentoIdentidad, BigDecimal monto, Integer plazo, Integer idTipoPrestamo) {
        return usuarioService.consultarUsuarioDocumento(documentoIdentidad)
                .switchIfEmpty(Mono.error(new SolicitudException(TipoError.USUARIO_NO_ENCONTRADO, documentoIdentidad)))
                .flatMap(usuario -> validarTipoPrestamo(idTipoPrestamo)
                        .then(Mono.just(usuario)))
                .flatMap(usuario -> registrarSolicitud(monto, plazo, usuario.getEmail(), idTipoPrestamo)
                        .then(Mono.just("Pendiente de revisión")));
    }
    
    private Mono<Void> validarTipoPrestamo(Integer idTipoPrestamo) {
        return solicitudRepository.existeIdTipoPrestamo(idTipoPrestamo)
                .filter(existe -> existe)
                .switchIfEmpty(Mono.error(new SolicitudException(TipoError.TIPO_PRESTAMO_INVALIDO, "ID: " + idTipoPrestamo)))
                .then();
    }
    
    private Mono<Void> registrarSolicitud(BigDecimal monto, Integer plazo, String email, Integer idTipoPrestamo) {
        Solicitud solicitud = new Solicitud(monto, plazo, email, Solicitud.ESTADO_PENDIENTE, idTipoPrestamo);
        return solicitudRepository.registrarSolicitud(solicitud);
    }
}