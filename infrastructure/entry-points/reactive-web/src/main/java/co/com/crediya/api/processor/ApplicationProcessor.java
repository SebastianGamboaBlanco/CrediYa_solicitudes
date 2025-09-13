package co.com.crediya.api.processor;

import co.com.crediya.api.dto.ApplicationRequest;
import co.com.crediya.api.dto.JwtUserInfo;
import co.com.crediya.api.helper.ValidationUtil;
import co.com.crediya.model.exceptions.LoanApplicationException;
import co.com.crediya.model.exceptions.ErrorType;
import co.com.crediya.usecase.createapplication.ApplicationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationProcessor {

    private final ApplicationUseCase ApplicationUseCase;
    private final ValidationUtil validationUtils;

    public Mono<String> processApplication(ApplicationRequest request, JwtUserInfo authenticatedUser) {
        MDC.put("operation", "create");
        MDC.put("authenticatedUser", authenticatedUser.getEmail());

        log.info("Processing application: requestDoc={}, authenticatedDoc={}, amount={}, term={}, loanType={}",
                request.getIdentityDocument(), authenticatedUser.getIdentityDocument(),
                request.getAmount(), request.getTermMonths(), request.getTypeId());

        return validationUtils.validateRequest(request)
                .doOnNext(req -> log.debug("Bean Validation checks completed"))
                .flatMap(validatedRequest -> ApplicationUseCase.create(
                        validatedRequest.getIdentityDocument(),
                        validatedRequest.getAmount(),
                        validatedRequest.getTermMonths(),
                        validatedRequest.getTypeId(),
                        authenticatedUser.getRoleId(),
                        authenticatedUser.getIdentityDocument()
                ))
                .doOnNext(result -> log.info("Application processed successfully: {}", result))
                .doOnError(error -> {
                    log.error("ERROR END - Error processing request - DocumentID: {}, Type: {}, Message: {}",
                            request.getIdentityDocument(), error.getClass().getSimpleName(), error.getMessage());
                });
    }
    

}