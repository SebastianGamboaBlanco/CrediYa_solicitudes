package co.com.crediya.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("solicitud")
public class SolicitudEntity {
    
    @Id
    @Column("id_solicitud")
    private Integer idSolicitud;
    
    @Column("monto")
    private BigDecimal monto;
    
    @Column("plazo")
    private Integer plazo;
    
    @Column("email")
    private String email;
    
    @Column("id_estado")
    private Integer idEstado;
    
    @Column("id_tipo_prestamo")
    private Integer idTipoPrestamo;
}