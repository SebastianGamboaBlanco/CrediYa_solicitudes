package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entities.TipoPrestamoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPrestamoReactiveRepository extends ReactiveCrudRepository<TipoPrestamoEntity, Integer> {
}