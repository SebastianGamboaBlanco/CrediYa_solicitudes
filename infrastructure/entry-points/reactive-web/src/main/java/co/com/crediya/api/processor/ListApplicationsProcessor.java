package co.com.crediya.api.processor;

import co.com.crediya.api.dto.JwtUserInfo;
import co.com.crediya.api.dto.ListApplicationsMapper;
import co.com.crediya.api.dto.ListApplicationsResponse;
import co.com.crediya.usecase.listapplications.ListApplicationsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListApplicationsProcessor {

    private final ListApplicationsUseCase listApplicationsUseCase;

    public Mono<ListApplicationsResponse> processListApplications(ServerRequest serverRequest) {
        JwtUserInfo userInfo = (JwtUserInfo) serverRequest.exchange().getAttribute("jwtUserInfo");

        Integer page = serverRequest.queryParam("page")
                .map(Integer::parseInt)
                .orElse(null);

        Integer size = serverRequest.queryParam("size")
                .map(Integer::parseInt)
                .orElse(null);

        List<Integer> statusIds = serverRequest.queryParam("statusIds")
                .map(param -> Arrays.stream(param.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList())
                .orElse(null);

        return listApplicationsUseCase.listApplications(userInfo.getRoleId(), page, size, statusIds)
                .map(ListApplicationsMapper::toResponse);
    }
}