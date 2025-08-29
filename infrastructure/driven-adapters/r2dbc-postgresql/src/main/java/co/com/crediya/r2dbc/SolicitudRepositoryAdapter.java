package co.com.crediya.r2dbc;

import co.com.crediya.model.Solicitud;
import co.com.crediya.model.gateways.SolicitudRepository;
import co.com.crediya.r2dbc.helpers.SolicitudMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class SolicitudRepositoryAdapter implements SolicitudRepository {
    
    private final SolicitudReactiveRepository solicitudReactiveRepository;
    private final TipoPrestamoReactiveRepository tipoPrestamoReactiveRepository;
    
    public SolicitudRepositoryAdapter(SolicitudReactiveRepository solicitudReactiveRepository,
                                      TipoPrestamoReactiveRepository tipoPrestamoReactiveRepository) {
        this.solicitudReactiveRepository = solicitudReactiveRepository;
        this.tipoPrestamoReactiveRepository = tipoPrestamoReactiveRepository;
        log.info("SolicitudRepositoryAdapter inicializado");
    }
    
    @Override
    @Transactional
    public Mono<Void> registrarSolicitud(Solicitud solicitud) {
        log.info("RepositoryAdapter: Registrando solicitud - monto={}, plazo={}", 
                solicitud.getMonto(), solicitud.getPlazo());
        
        return Mono.just(solicitud)
                .map(SolicitudMapper::toEntity)
                .doOnNext(entity -> log.info("RepositoryAdapter: Entidad mapeada"))
                .flatMap(solicitudReactiveRepository::save)
                .doOnNext(saved -> log.info("RepositoryAdapter: Solicitud guardada con ID={}", saved.getIdSolicitud()))
                .then();
    }
    
    @Override
    @Transactional
    public Mono<Boolean> existeIdTipoPrestamo(Integer idTipoPrestamo) {
        log.info("RepositoryAdapter: Validando existencia de tipo préstamo ID={}", idTipoPrestamo);
        
        return tipoPrestamoReactiveRepository.existsById(idTipoPrestamo)
                .doOnNext(existe -> log.info("RepositoryAdapter: Tipo préstamo ID={} existe={}", idTipoPrestamo, existe));
    }
}