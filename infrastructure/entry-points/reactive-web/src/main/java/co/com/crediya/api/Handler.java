package co.com.crediya.api;

import co.com.crediya.api.dto.ApplicationRequest;
import co.com.crediya.api.dto.ApplicationResponse;
import co.com.crediya.api.dto.ApplicationDTOMapper;
import co.com.crediya.api.exception.ErrorHandler;
import co.com.crediya.api.processor.ApplicationProcessor;
import co.com.crediya.api.processor.ListApplicationsProcessor;
import co.com.crediya.api.dto.JwtUserInfo;
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
    private final ListApplicationsProcessor listApplicationsProcessor;
    private final ErrorHandler errorHandler;

    public Mono<ServerResponse> createApplication(ServerRequest serverRequest) {
        JwtUserInfo userInfo = (JwtUserInfo) serverRequest.exchange().getAttribute("jwtUserInfo");

        return serverRequest.bodyToMono(ApplicationRequest.class)
                .flatMap(request -> applicationProcessor.processApplication(request, userInfo))
                .flatMap(response -> {
                    ApplicationResponse result = ApplicationDTOMapper.toSuccessResponse(response);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(result);
                })
                .onErrorResume(errorHandler::handleError);
    }

    public Mono<ServerResponse> listApplications(ServerRequest serverRequest) {
        return listApplicationsProcessor.processListApplications(serverRequest)
                .flatMap(applicationsResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(applicationsResponse))
                .onErrorResume(errorHandler::handleError);
    }
}
