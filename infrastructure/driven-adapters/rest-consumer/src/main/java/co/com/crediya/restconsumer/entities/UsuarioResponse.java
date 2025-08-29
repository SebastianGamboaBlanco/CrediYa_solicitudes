package co.com.crediya.restconsumer.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private String status;
    private String mensaje;
    private UsuarioData usuario;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioData {
        private Integer id;
        private String nombres;
        private String apellidos;
        
        @JsonProperty("correoElectronico")
        private String correoElectronico;
        
        @JsonProperty("documentoIdentidad")
        private String documentoIdentidad;
        
        @JsonProperty("fechaNacimiento")
        private String fechaNacimiento;
        
        private String telefono;
        
        @JsonProperty("idRol")
        private Integer idRol;
        
        @JsonProperty("salarioBase")
        private BigDecimal salarioBase;
    }
}