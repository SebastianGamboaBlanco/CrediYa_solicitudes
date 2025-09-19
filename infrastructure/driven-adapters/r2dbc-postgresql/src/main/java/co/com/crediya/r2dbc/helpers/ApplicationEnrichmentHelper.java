package co.com.crediya.r2dbc.helpers;

import co.com.crediya.model.ListApplications;
import co.com.crediya.model.PaginatedListApplications;
import co.com.crediya.model.gateways.UserService;
import co.com.crediya.r2dbc.dto.ListApplicationsQueryResult;
import co.com.crediya.r2dbc.dto.PaginatedApplicationsResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ApplicationEnrichmentHelper {

    private final UserService userService;

    public ApplicationEnrichmentHelper(UserService userService) {
        this.userService = userService;
    }

    public Mono<PaginatedListApplications> enrichApplicationsWithUserData(PaginatedApplicationsResponse response) {
        Flux<ListApplications> enrichedApplications = Flux.fromIterable(response.getApplications())
                .flatMap(this::enrichWithUserData);

        return enrichedApplications.collectList()
                .map(applications -> new PaginatedListApplications(
                        applications,
                        response.getTotalRecords(),
                        response.getPageNumber(),
                        response.getPageSize()
                ));
    }

    private Mono<ListApplications> enrichWithUserData(ListApplicationsQueryResult queryResult) {
        return userService.getUserByEmail(queryResult.getEmail())
                .map(user -> new ListApplications(
                        queryResult.getIdSolicitud(),
                        user.getFullName(),
                        queryResult.getEmail(),
                        user.getBaseSalary(),
                        queryResult.getMonto(),
                        queryResult.getPlazo(),
                        queryResult.getTasaInteres(),
                        queryResult.getIdTipoPrestamo(),
                        queryResult.getTipoPrestamo(),
                        queryResult.getIdEstado(),
                        queryResult.getEstadoSolicitud()
                ))
                .doOnError(error -> log.error("Error enriching data for email={}: {}",
                        queryResult.getEmail(), error.getMessage()));
    }
}