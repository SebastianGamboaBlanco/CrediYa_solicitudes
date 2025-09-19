package co.com.crediya.r2dbc.helpers;

import co.com.crediya.r2dbc.dto.PaginatedApplicationsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class JsonParsingService {

    private final ObjectMapper objectMapper;

    public JsonParsingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Mono<PaginatedApplicationsResponse> parseJsonResponse(String jsonResult) {
        try {
            PaginatedApplicationsResponse response = objectMapper.readValue(jsonResult, PaginatedApplicationsResponse.class);
            return Mono.just(response);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON response: {}", e.getMessage());
            return Mono.error(new RuntimeException("Error parsing database response", e));
        }
    }
}