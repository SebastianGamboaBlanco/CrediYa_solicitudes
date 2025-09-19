package co.com.crediya.model.gateways;

import co.com.crediya.model.LoanApplication;
import co.com.crediya.model.PaginatedListApplications;
import reactor.core.publisher.Mono;
import java.util.List;

public interface ApplicationRepository {
    Mono<Void> create(LoanApplication loanApplication);
    Mono<Boolean> existsLoanTypeId(Integer loanTypeId);
    Mono<PaginatedListApplications> findApplicationsByStatusIds(List<Integer> statusIds, Integer page, Integer size);
}
