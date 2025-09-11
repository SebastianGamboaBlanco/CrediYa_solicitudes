package co.com.crediya.restconsumer;

import co.com.crediya.model.User;
import co.com.crediya.model.gateways.UserService;
import co.com.crediya.restconsumer.processor.UserResponseProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class UserRestAdapter implements UserService {
    
    private final WebClient webClient;
    private final UserResponseProcessor responseProcessor;
    
    public UserRestAdapter(WebClient.Builder webClientBuilder, String userServiceUrl, UserResponseProcessor responseProcessor) {
        this.webClient = webClientBuilder.baseUrl(userServiceUrl).build();
        this.responseProcessor = responseProcessor;
        log.info("UserRestAdapter initialized with URL: {}", userServiceUrl);
    }
    
    @Override
    public Mono<User> getUserByDocument(String identityDocument) {
        log.info("RestAdapter: Querying user with document: {}", identityDocument);
        
        return webClient.get()
                .uri("/api/v1/users/{document}", identityDocument)
                .exchangeToMono(responseProcessor::processResponse);
    }
}