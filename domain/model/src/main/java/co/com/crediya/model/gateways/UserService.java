package co.com.crediya.model.gateways;

import co.com.crediya.model.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> getUserByDocument(String identityDocument);
}