package co.com.crediya.restconsumer.mappers;

import co.com.crediya.model.Usuario;
import co.com.crediya.restconsumer.entities.UsuarioResponse;

public class UsuarioMapper {
    
    public static Usuario toDomain(UsuarioResponse response) {
        return new Usuario(response.getUsuario().getCorreoElectronico());
    }
}