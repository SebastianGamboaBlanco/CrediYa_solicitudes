package co.com.crediya.api.security;

import co.com.crediya.api.dto.JwtUserInfo;
import co.com.crediya.api.exception.ErrorHandler;
import co.com.crediya.model.exceptions.ErrorType;
import co.com.crediya.model.exceptions.LoanApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationWebFilter implements WebFilter {
    
    private final JwtTokenValidator jwtTokenValidator;
    private final ErrorHandler errorHandler;
    private static final String BEARER_PREFIX = "Bearer ";

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/actuator",
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars"
    );
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        if (isExcludedPath(path)) {
            return chain.filter(exchange);
        }
        
        log.debug("JWT Authentication filter applied to protected path: {}", path);
        
        return extractToken(exchange)
                .switchIfEmpty(Mono.error(() -> {
                    log.warn("Access denied - No Authorization header found for protected path: {}", path);
                    return new LoanApplicationException(ErrorType.UNAUTHORIZED, 
                            "Authentication required - Bearer token missing");
                }))
                .flatMap(this::validateToken)
                .switchIfEmpty(Mono.error(() -> {
                    log.warn("Access denied - Invalid JWT token for protected path: {}", path);
                    return new LoanApplicationException(ErrorType.INVALID_TOKEN, 
                            "Invalid or expired authentication token");
                }))
                .flatMap(userInfo -> {
                    exchange.getAttributes().put("jwtUserInfo", userInfo);
                    log.info("User info stored in exchange attributes: {}", userInfo.getEmail());
                    
                    return chain.filter(exchange);
                })
                .onErrorResume( error ->
                        errorHandler.handleJwtAuthenticationError(exchange,error));
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(excludedPath -> {
            if (excludedPath.contains("{")) {
                String basePattern = excludedPath.substring(0, excludedPath.indexOf("{"));
                return path.startsWith(basePattern);
            }
            return path.startsWith(excludedPath);
        });
    }
    
    private Mono<String> extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            log.debug("JWT token extracted from Authorization header");
            return Mono.just(token);
        }
        
        log.debug("No JWT token found in Authorization header");
        return Mono.empty();
    }
    
    private Mono<JwtUserInfo> validateToken(String token) {
        return jwtTokenValidator.validateTokenAndExtractUser(token)
                .doOnNext(userInfo -> log.info("User authenticated: {} ({})", 
                        userInfo.getEmail(), userInfo.getRoleName()))
                .doOnError(error -> log.warn("JWT authentication failed: {}", error.getMessage()))
                .onErrorResume(error -> {
                    // Si hay error en la validación, retornar empty para que switchIfEmpty maneje el 401
                    log.debug("Token validation failed, will return 401");
                    return Mono.empty();
                });
    }
    
}