package co.com.crediya.api.security;

import co.com.crediya.api.dto.JwtUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {
    
    private final JwtService jwtService;
    
    public Mono<JwtUserInfo> validateTokenAndExtractUser(String token) {
        return Mono.fromCallable(() -> {
            if (!jwtService.validateToken(token)) {
                log.warn("Invalid JWT token");
                throw new RuntimeException("Invalid JWT token");
            }

            JwtUserInfo userInfo = jwtService.extractUserInfo(token);

            log.info("JWT validated successfully for user: {} with role: {} ({})",
                    userInfo.getEmail(), userInfo.getRoleId(), userInfo.getRoleName());
            return userInfo;
        })
        .onErrorResume(exception -> {
            log.error("Error validating JWT token: {}", exception.getMessage());
            return Mono.empty();
        });
    }
}