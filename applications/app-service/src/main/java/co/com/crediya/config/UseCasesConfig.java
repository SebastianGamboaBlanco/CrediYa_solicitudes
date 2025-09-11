package co.com.crediya.config;

import co.com.crediya.model.gateways.ApplicationRepository;
import co.com.crediya.model.gateways.UserService;
import co.com.crediya.restconsumer.UserRestAdapter;
import co.com.crediya.restconsumer.processor.UserResponseProcessor;
import co.com.crediya.usecase.createapplication.ApplicationUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UseCasesConfig {

    @Bean
    public UserResponseProcessor userResponseProcessor() {
        return new UserResponseProcessor();
    }

    @Bean
    public UserService userService(@Value("${app.user-service.url}") String userServiceUrl,
                                      UserResponseProcessor userResponseProcessor) {
        WebClient.Builder webClientBuilder = WebClient.builder();
        return new UserRestAdapter(webClientBuilder, userServiceUrl, userResponseProcessor);
    }

    @Bean
    public ApplicationUseCase applicationUseCase(UserService userService, ApplicationRepository applicationRepository) {
        return new ApplicationUseCase(userService, applicationRepository);
    }
}
