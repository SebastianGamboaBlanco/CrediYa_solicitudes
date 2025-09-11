package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entities.TypeEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeReactiveRepository extends ReactiveCrudRepository<TypeEntity, Integer> {
}