package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entities.SolicitudEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Integer> {
}