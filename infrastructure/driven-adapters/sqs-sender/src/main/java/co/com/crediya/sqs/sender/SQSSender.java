package co.com.crediya.sqs.sender;

import co.com.crediya.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender /*implements SomeGateway*/ {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;

    public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        SendMessageRequest.Builder builder = SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message);

        if (properties.queueUrl().endsWith(".fifo")) {
            builder.messageGroupId("solicitudes-status-updates")
                   .messageDeduplicationId(generateDeduplicationId(message));
        }

        return builder.build();
    }

    private String generateDeduplicationId(String message) {
        return String.valueOf(message.hashCode());
    }
}
