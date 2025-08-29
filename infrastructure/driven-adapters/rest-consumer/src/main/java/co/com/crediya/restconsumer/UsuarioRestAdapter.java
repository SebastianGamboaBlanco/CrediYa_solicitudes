package co.com.crediya.restconsumer;

import co.com.crediya.model.Usuario;
import co.com.crediya.model.gateways.UsuarioService;
import co.com.crediya.restconsumer.entities.UsuarioResponse;
import co.com.crediya.restconsumer.mappers.UsuarioMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class UsuarioRestAdapter implements UsuarioService {
    
    private final WebClient webClient;
    
    public UsuarioRestAdapter(WebClient.Builder webClientBuilder, String usuarioServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(usuarioServiceUrl).build();
        log.info("UsuarioRestAdapter inicializado con URL: {}", usuarioServiceUrl);
    }
    
    @Override
    public Mono<Usuario> consultarUsuarioDocumento(String documentoIdentidad) {
        String url = "/api/v1/usuarios/" + documentoIdentidad;
        log.info("RestAdapter: Consultando usuario - URL: {}", url);
        
        return webClient.get()
                .uri("/api/v1/usuarios/{documento}", documentoIdentidad)
                .retrieve()
                .bodyToMono(UsuarioResponse.class)
                .doOnNext(response -> log.info("RestAdapter: Response parseado - status={}, email={}", 
                        response.getStatus(), 
                        response.getUsuario() != null ? response.getUsuario().getCorreoElectronico() : "null"))
                .filter(response -> "200".equals(response.getStatus()))
                .doOnNext(response -> log.info("RestAdapter: Respuesta válida (status 200)"))
                .map(UsuarioMapper::toDomain)
                .doOnNext(usuario -> log.info("RestAdapter: Usuario mapeado - email={}", usuario.getEmail()));
    }
}