package co.com.crediya.restconsumer;

import co.com.crediya.model.User;
import co.com.crediya.model.gateways.UserService;
import co.com.crediya.restconsumer.entities.UserData;
import co.com.crediya.restconsumer.processor.UserDataResponseProcessor;
import co.com.crediya.restconsumer.processor.UserResponseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class UserRestAdapter implements UserService {
    
    private final WebClient webClient;
    private final UserResponseProcessor responseProcessor;
    private final UserDataResponseProcessor userDataResponseProcessor;

    public UserRestAdapter(WebClient.Builder webClientBuilder, String userServiceUrl,
                          UserResponseProcessor responseProcessor,
                          UserDataResponseProcessor userDataResponseProcessor) {
        this.webClient = webClientBuilder.baseUrl(userServiceUrl).build();
        this.responseProcessor = responseProcessor;
        this.userDataResponseProcessor = userDataResponseProcessor;
        log.info("UserRestAdapter initialized with URL: {}", userServiceUrl);
    }
    
    @Override
    public Mono<User> getUserByDocument(String identityDocument) {
        log.info("RestAdapter: Querying user with document: {}", identityDocument);

        return webClient.get()
                .uri("/api/v1/users/{document}", identityDocument)
                .exchangeToMono(responseProcessor::processResponse);
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        log.info("RestAdapter: Querying user with email: {}", email);

        return webClient.get()
                .uri("/api/v1/users/email/{email}", email)
                .exchangeToMono(userDataResponseProcessor::processResponse);
    }
}