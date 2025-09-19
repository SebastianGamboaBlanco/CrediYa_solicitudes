package co.com.crediya.r2dbc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListApplicationsQueryResult {
    @Column("id_solicitud")
    @JsonProperty("id_solicitud")
    private Integer idSolicitud;

    @Column("monto")
    @JsonProperty("monto")
    private BigDecimal monto;

    @Column("plazo")
    @JsonProperty("plazo")
    private Integer plazo;

    @Column("email")
    @JsonProperty("email")
    private String email;

    @Column("id_estado")
    @JsonProperty("id_estado")
    private Integer idEstado;

    @Column("id_tipo_prestamo")
    @JsonProperty("id_tipo_prestamo")
    private Integer idTipoPrestamo;

    @Column("tasa_interes")
    @JsonProperty("tasa_interes")
    private BigDecimal tasaInteres;

    @Column("tipo_prestamo")
    @JsonProperty("tipo_prestamo")
    private String tipoPrestamo;

    @Column("estado_solicitud")
    @JsonProperty("estado_solicitud")
    private String estadoSolicitud;
}