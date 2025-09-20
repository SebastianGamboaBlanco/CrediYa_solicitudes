package co.com.crediya.sqs.sender;

import co.com.crediya.model.ApplicationStatusInfo;
import co.com.crediya.model.gateways.MessageSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageSenderAdapter implements MessageSender {

    private final SQSSender sqsSender;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public Mono<String> sendStatusUpdate(ApplicationStatusInfo message) {
        return Mono.fromCallable(() -> serializeMessage(message))
                .flatMap(sqsSender::send)
                .doOnSuccess(messageId -> log.info("Status update sent to SQS with messageId: {}", messageId))
                .doOnError(error -> log.error("Error sending status update to SQS: {}", error.getMessage()));
    }

    private String serializeMessage(ApplicationStatusInfo message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(message);
    }
}