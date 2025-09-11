package co.com.crediya.api.processor;

import co.com.crediya.api.dto.ApplicationRequest;
import co.com.crediya.api.helper.ValidationUtil;
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

    public Mono<String> processApplication(ApplicationRequest request) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "");
            MDC.put("traceId", traceId);
            MDC.put("operation", "create");

            log.info("Processing application: doc={}, amount={}, term={}, loanType={}",
                    request.getIdentityDocument(), request.getAmount(), request.getTermMonths(), request.getTypeId());

            return validationUtils.validateRequest(request)
                    .doOnNext(req -> {
                        log.debug("Bean Validation checks completed");
                    })
                    .flatMap(validatedRequest -> ApplicationUseCase.create(
                            validatedRequest.getIdentityDocument(),
                            validatedRequest.getAmount(),
                            validatedRequest.getTermMonths(),
                            validatedRequest.getTypeId()
                    ))
                    .doOnNext(result -> log.info("Application processed successfully: {}", result))
                    .doOnError(error -> {
                        MDC.put("traceId", traceId);
                        log.error("ERROR END - Error processing request - DocumentID: {}, Type: {}, Message: {}",
                                request.getIdentityDocument(), error.getClass().getSimpleName(), error.getMessage());
                    });
        });
    }

}