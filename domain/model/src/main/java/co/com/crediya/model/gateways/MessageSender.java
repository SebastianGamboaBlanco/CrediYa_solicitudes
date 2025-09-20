package co.com.crediya.model.gateways;

import co.com.crediya.model.ApplicationStatusInfo;
import reactor.core.publisher.Mono;

public interface MessageSender {
    Mono<String> sendStatusUpdate(ApplicationStatusInfo message);
}