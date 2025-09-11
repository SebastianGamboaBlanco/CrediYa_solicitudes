package co.com.crediya.model.gateways;

import co.com.crediya.model.LoanApplication;
import reactor.core.publisher.Mono;

public interface ApplicationRepository {
    Mono<Void> create(LoanApplication loanApplication);
    Mono<Boolean> existsLoanTypeId(Integer loanTypeId);
}
