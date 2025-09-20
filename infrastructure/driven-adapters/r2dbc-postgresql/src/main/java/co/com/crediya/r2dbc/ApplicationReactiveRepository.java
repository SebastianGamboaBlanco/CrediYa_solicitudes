package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entities.ApplicationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.util.List;

@Repository
public interface ApplicationReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, Integer> {

    @Query("SELECT list_applications($1, $2, $3)")
    Mono<String> findApplicationsByStatusIdsWithPagination(Integer[] statusIds, Integer pageNumber, Integer pageSize);

    @Query("UPDATE solicitud SET id_estado = $2 WHERE id_solicitud = $1")
    Mono<Void> updateApplicationStatus(Integer applicationId, Integer statusId);

}