package co.com.crediya.api.processor;

import co.com.crediya.api.dto.JwtUserInfo;
import co.com.crediya.api.dto.UpdateApplicationStatusRequest;
import co.com.crediya.usecase.updatestatus.UpdateApplicationStatusUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateApplicationStatusProcessor {

    private final UpdateApplicationStatusUseCase updateApplicationStatusUseCase;

    public Mono<String> processUpdateApplicationStatus(UpdateApplicationStatusRequest request, JwtUserInfo userInfo) {
        log.info("Processing update application status request for applicationId: {} with statusId: {}",
                request.getApplicationId(), request.getStatusId());

        return updateApplicationStatusUseCase.updateApplicationStatus(
                request.getApplicationId(),
                request.getStatusId(),
                userInfo.getRoleId()
        );
    }
}