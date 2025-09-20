package co.com.crediya.config;

import co.com.crediya.model.gateways.ApplicationRepository;
import co.com.crediya.model.gateways.UserService;
import co.com.crediya.model.gateways.MessageSender;
import co.com.crediya.sqs.sender.MessageSenderAdapter;
import co.com.crediya.sqs.sender.SQSSender;
import co.com.crediya.restconsumer.UserRestAdapter;
import co.com.crediya.restconsumer.processor.UserDataResponseProcessor;
import co.com.crediya.restconsumer.processor.UserResponseProcessor;
import co.com.crediya.usecase.createapplication.ApplicationUseCase;
import co.com.crediya.usecase.listapplications.ListApplicationsUseCase;
import co.com.crediya.usecase.updatestatus.UpdateApplicationStatusUseCase;
import co.com.crediya.r2dbc.helpers.JsonParsingService;
import co.com.crediya.r2dbc.helpers.ApplicationEnrichmentHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    public UserDataResponseProcessor userDataResponseProcessor() {
        return new UserDataResponseProcessor();
    }

    @Bean
    public UserService userService(@Value("${app.user-service.url}") String userServiceUrl,
                                      UserResponseProcessor userResponseProcessor,
                                      UserDataResponseProcessor userDataResponseProcessor) {
        WebClient.Builder webClientBuilder = WebClient.builder();
        return new UserRestAdapter(webClientBuilder, userServiceUrl, userResponseProcessor, userDataResponseProcessor);
    }

    @Bean
    public ApplicationUseCase applicationUseCase(UserService userService, ApplicationRepository applicationRepository) {
        return new ApplicationUseCase(userService, applicationRepository);
    }

    @Bean
    public JsonParsingService jsonParsingService(ObjectMapper objectMapper) {
        return new JsonParsingService(objectMapper);
    }

    @Bean
    public ApplicationEnrichmentHelper applicationEnrichmentHelper(UserService userService) {
        return new ApplicationEnrichmentHelper(userService);
    }

    @Bean
    public ListApplicationsUseCase listApplicationsUseCase(ApplicationRepository applicationRepository) {
        return new ListApplicationsUseCase(applicationRepository);
    }

    @Bean
    public MessageSender messageSender(SQSSender sqsSender) {
        return new MessageSenderAdapter(sqsSender);
    }

    @Bean
    public UpdateApplicationStatusUseCase updateApplicationStatusUseCase(ApplicationRepository applicationRepository, MessageSender messageSender) {
        return new UpdateApplicationStatusUseCase(applicationRepository, messageSender);
    }
}
