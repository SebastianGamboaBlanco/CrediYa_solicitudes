package co.com.crediya.config;

import co.com.crediya.model.gateways.SolicitudRepository;
import co.com.crediya.model.gateways.UsuarioService;
import co.com.crediya.restconsumer.UsuarioRestAdapter;
import co.com.crediya.usecase.registrarsolicitud.RegistrarSolicitudUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UseCasesConfig {

    @Bean
    public UsuarioService usuarioService(@Value("${app.usuario-service.url}") String usuarioServiceUrl) {
        WebClient.Builder webClientBuilder = WebClient.builder();
        return new UsuarioRestAdapter(webClientBuilder, usuarioServiceUrl);
    }

    @Bean
    public RegistrarSolicitudUseCase registrarSolicitudUseCase(UsuarioService usuarioService, SolicitudRepository solicitudRepository) {
        return new RegistrarSolicitudUseCase(usuarioService, solicitudRepository);
    }
}
