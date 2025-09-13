package co.com.crediya.restconsumer.processor;

import co.com.crediya.model.User;
import co.com.crediya.model.exceptions.LoanApplicationException;
import co.com.crediya.restconsumer.entities.UserResponse;
import co.com.crediya.restconsumer.mappers.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserResponseProcessor {

    public Mono<User> processResponse(ClientResponse response) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(UserResponse.class)
                    .map(UserMapper::toDomain)
                    .doOnNext(user -> log.info("RestAdapter: User mapped - email={}", user.getEmail()));
        } else {
            return response.bodyToMono(UserResponse.class)
                    .flatMap(errorResponse -> {
                        log.warn("RestAdapter: External service error - status={}, code={}, message={}", 
                                response.statusCode(), errorResponse.getCode(), errorResponse.getMessage());
                        
                        return Mono.<User>error(new LoanApplicationException(errorResponse.getMessage()));
                    });
        }
    }
}