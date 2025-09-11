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
public class ApplicationEntity {
    
    @Id
    @Column("id_solicitud")
    private Integer ApplicationId;
    
    @Column("monto")
    private BigDecimal amount;
    
    @Column("plazo")
    private Integer termMonths;
    
    @Column("email")
    private String email;
    
    @Column("id_estado")
    private Integer statusId;
    
    @Column("id_tipo_prestamo")
    private Integer typeId;
}