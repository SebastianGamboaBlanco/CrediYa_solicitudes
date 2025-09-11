package co.com.crediya.api;

import co.com.crediya.api.dto.ApplicationRequest;
import co.com.crediya.api.dto.ApplicationResponse;
import co.com.crediya.api.dto.ApplicationDTOMapper;
import co.com.crediya.api.exception.ErrorHandler;
import co.com.crediya.api.processor.ApplicationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    
    private final ApplicationProcessor applicationProcessor;
    private final ErrorHandler errorHandler;

    public Mono<ServerResponse> createApplication(ServerRequest serverRequest) {
        log.info("=== STARTING LOAN APPLICATION REGISTRATION ===");
        
        return serverRequest.bodyToMono(ApplicationRequest.class)
                .doOnNext(request -> log.info("Request received: doc={}, amount={}, term={}, loanType={}", 
                        request.getIdentityDocument(), request.getAmount(), request.getTermMonths(), request.getTypeId()))
                .flatMap(applicationProcessor::processApplication)
                .flatMap(response -> {
                    ApplicationResponse result = ApplicationDTOMapper.toSuccessResponse(response);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(result);
                })
                .onErrorResume(errorHandler::handleError);

    }
}
