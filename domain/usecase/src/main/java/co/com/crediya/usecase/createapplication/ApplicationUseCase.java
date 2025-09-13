package co.com.crediya.usecase.createapplication;

import co.com.crediya.model.LoanApplication;
import co.com.crediya.model.exceptions.LoanApplicationException;
import co.com.crediya.model.exceptions.ErrorType;
import co.com.crediya.model.gateways.ApplicationRepository;
import co.com.crediya.model.gateways.UserService;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class ApplicationUseCase {
    private final UserService userService;
    private final ApplicationRepository ApplicationRepository;
    
    public ApplicationUseCase(UserService userService, ApplicationRepository loanApplicationRepository) {
        this.userService = userService;
        this.ApplicationRepository = loanApplicationRepository;
    }
    
    public Mono<String> create(String identityDocument, BigDecimal amount, Integer termMonths, Integer loanTypeId, Integer userRoleId, String authenticatedUserDocument) {
        return validateUserRole(userRoleId)
                .then(validateUserOwnership(identityDocument, authenticatedUserDocument))
                .then(userService.getUserByDocument(identityDocument)
                        .switchIfEmpty(Mono.error(new LoanApplicationException(ErrorType.USER_NOT_FOUND, identityDocument))))
                .flatMap(user -> validateLoanType(loanTypeId)
                        .then(Mono.just(user)))
                .flatMap(user -> createLoanApplication(amount, termMonths, user.getEmail(), loanTypeId)
                        .then(Mono.just("Pending review")));
    }
    
    private Mono<Void> validateUserRole(Integer roleId) {
        if (roleId == null || !roleId.equals(3)) {
            return Mono.error(new LoanApplicationException(ErrorType.FORBIDDEN,
                    "Client role required for loan applications. Current role ID: " + roleId));
        }
        return Mono.empty();
    }

    private Mono<Void> validateUserOwnership(String requestedDocument, String authenticatedDocument) {
        if (!requestedDocument.equals(authenticatedDocument)) {
            return Mono.error(new LoanApplicationException(ErrorType.FORBIDDEN,
                    "Users can only create loan applications for themselves. Requested: " + requestedDocument + ", Authenticated: " + authenticatedDocument));
        }
        return Mono.empty();
    }

    private Mono<Void> validateLoanType(Integer loanTypeId) {
        return ApplicationRepository.existsLoanTypeId(loanTypeId)
                .filter(existe -> existe)
                .switchIfEmpty(Mono.error(new LoanApplicationException(ErrorType.INVALID_LOAN_TYPE, "ID: " + loanTypeId)))
                .then();
    }
    
    private Mono<Void> createLoanApplication(BigDecimal amount, Integer termMonths, String email, Integer loanTypeId) {
        LoanApplication loanApplication = new LoanApplication(amount, termMonths, email, LoanApplication.PENDING_STATUS, loanTypeId);
        return ApplicationRepository.create(loanApplication);
    }
}