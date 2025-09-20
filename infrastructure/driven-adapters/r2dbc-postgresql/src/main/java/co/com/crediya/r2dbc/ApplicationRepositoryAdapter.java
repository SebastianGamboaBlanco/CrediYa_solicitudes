package co.com.crediya.r2dbc;

import co.com.crediya.model.LoanApplication;
import co.com.crediya.model.PaginatedListApplications;
import co.com.crediya.model.ApplicationStatusInfo;
import co.com.crediya.model.gateways.ApplicationRepository;
import co.com.crediya.r2dbc.helpers.ApplicationMapper;
import co.com.crediya.r2dbc.helpers.JsonParsingService;
import co.com.crediya.r2dbc.helpers.ApplicationEnrichmentHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@Repository
public class ApplicationRepositoryAdapter implements ApplicationRepository {

    private final ApplicationReactiveRepository applicationReactiveRepository;
    private final TypeReactiveRepository typeReactiveRepository;
    private final StatusReactiveRepository statusReactiveRepository;
    private final JsonParsingService jsonParsingService;
    private final ApplicationEnrichmentHelper enrichmentHelper;

    public ApplicationRepositoryAdapter(ApplicationReactiveRepository applicationReactiveRepository,
                                        TypeReactiveRepository typeReactiveRepository,
                                        StatusReactiveRepository statusReactiveRepository,
                                        JsonParsingService jsonParsingService,
                                        ApplicationEnrichmentHelper enrichmentHelper) {
        this.applicationReactiveRepository = applicationReactiveRepository;
        this.typeReactiveRepository = typeReactiveRepository;
        this.statusReactiveRepository = statusReactiveRepository;
        this.jsonParsingService = jsonParsingService;
        this.enrichmentHelper = enrichmentHelper;
        log.info("ApplicationRepositoryAdapter initialized");
    }

    @Override
    @Transactional
    public Mono<Void> create(LoanApplication loanApplication) {
        return Mono.just(loanApplication)
                .map(ApplicationMapper::toEntity)
                .flatMap(applicationReactiveRepository::save)
                .then();
    }

    @Override
    @Transactional
    public Mono<Boolean> existsLoanTypeId(Integer loanTypeId) {
        return typeReactiveRepository.existsById(loanTypeId);
    }

    @Override
    public Mono<PaginatedListApplications> findApplicationsByStatusIds(List<Integer> statusIds, Integer page, Integer size) {
        Integer[] statusIdsArray = statusIds.toArray(new Integer[0]);
        return applicationReactiveRepository.findApplicationsByStatusIdsWithPagination(statusIdsArray, page, size)
                .flatMap(jsonParsingService::parseJsonResponse)
                .flatMap(enrichmentHelper::enrichApplicationsWithUserData);
    }

    @Override
    @Transactional
    public Mono<Boolean> existsApplicationId(Integer applicationId) {
        return applicationReactiveRepository.existsById(applicationId);
    }

    @Override
    @Transactional
    public Mono<Void> updateStatus(Integer applicationId, Integer statusId) {
        return applicationReactiveRepository.updateApplicationStatus(applicationId, statusId);
    }

    @Override
    @Transactional
    public Mono<ApplicationStatusInfo> getApplicationWithStatus(Integer applicationId) {
        return applicationReactiveRepository.findById(applicationId)
                .flatMap(application ->
                    statusReactiveRepository.findById(application.getStatusId())
                        .map(status -> new ApplicationStatusInfo(
                                application.getApplicationId(),
                                application.getEmail(),
                                status.getName()
                        ))
                );
    }

}