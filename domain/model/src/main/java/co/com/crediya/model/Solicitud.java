package co.com.crediya.model;

import java.math.BigDecimal;

public class Solicitud {
    public static final Integer ESTADO_PENDIENTE = 1;

    private final BigDecimal monto;
    private final Integer plazo;
    private final String email;
    private final Integer idEstado;
    private final Integer idTipoPrestamo;

    public Solicitud(BigDecimal monto, Integer plazo, String email, Integer idEstado, Integer idTipoPrestamo) {

        this.monto = monto;
        this.plazo = plazo;
        this.email = email;
        this.idEstado = idEstado;
        this.idTipoPrestamo = idTipoPrestamo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public String getEmail() {
        return email;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public Integer getIdTipoPrestamo() {
        return idTipoPrestamo;
    }

}
