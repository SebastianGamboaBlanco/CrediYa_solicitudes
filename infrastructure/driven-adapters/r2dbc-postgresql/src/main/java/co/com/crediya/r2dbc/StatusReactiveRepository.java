package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entities.StatusEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusReactiveRepository extends ReactiveCrudRepository<StatusEntity, Integer> {
}