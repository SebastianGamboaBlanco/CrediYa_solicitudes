package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entities.ApplicationEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, Integer> {
}