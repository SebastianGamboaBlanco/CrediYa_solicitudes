package co.com.crediya.model.gateways;

import co.com.crediya.model.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioService {
    Mono<Usuario> consultarUsuarioDocumento(String cedula);
}