package co.com.crediya.r2dbc;

import co.com.crediya.model.LoanApplication;
import co.com.crediya.model.gateways.ApplicationRepository;
import co.com.crediya.r2dbc.helpers.ApplicationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class ApplicationRepositoryAdapter implements ApplicationRepository {

    private final ApplicationReactiveRepository applicationReactiveRepository;
    private final TypeReactiveRepository typeReactiveRepository;

    public ApplicationRepositoryAdapter(ApplicationReactiveRepository applicationReactiveRepository,
                                        TypeReactiveRepository typeReactiveRepository) {
        this.applicationReactiveRepository = applicationReactiveRepository;
        this.typeReactiveRepository = typeReactiveRepository;
        log.info("LoanApplicationRepositoryAdapter initialized");
    }

    @Override
    @Transactional
    public Mono<Void> create(LoanApplication loanApplication) {
        log.info("RepositoryAdapter: Creating loan application - amount={}, termMonths={}",
                loanApplication.getAmount(), loanApplication.getTermMonths());

        return Mono.just(loanApplication)
                .map(ApplicationMapper::toEntity)
                .doOnNext(entity -> log.info("RepositoryAdapter: Entity mapped"))
                .flatMap(applicationReactiveRepository::save)
                .doOnNext(saved -> log.info("RepositoryAdapter: Loan application saved with ID={}", saved.getApplicationId()))
                .then();
    }

    @Override
    @Transactional
    public Mono<Boolean> existsLoanTypeId(Integer loanTypeId) {
        log.info("RepositoryAdapter: Validating loan type existence ID={}", loanTypeId);

        return typeReactiveRepository.existsById(loanTypeId)
                .doOnNext(exists -> log.info("RepositoryAdapter: Loan type ID={} exists={}", loanTypeId, exists));
    }
}