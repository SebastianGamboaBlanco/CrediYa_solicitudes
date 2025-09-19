package co.com.crediya.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ListApplications {
    private final Integer idSolicitud;
    private final String nombre;
    private final String email;
    private final BigDecimal salarioBase;
    private final BigDecimal monto;
    private final Integer plazo;
    private final BigDecimal tasaInteres;
    private final Integer idTipoPrestamo;
    private final String tipoPrestamo;
    private final Integer idEstado;
    private final String estadoSolicitud;
    private final BigDecimal deudaTotalMensual;

    public ListApplications(Integer idSolicitud, String nombre, String email, BigDecimal salarioBase,
                           BigDecimal monto, Integer plazo, BigDecimal tasaInteres, Integer idTipoPrestamo,
                           String tipoPrestamo, Integer idEstado, String estadoSolicitud) {
        this.idSolicitud = idSolicitud;
        this.nombre = nombre;
        this.email = email;
        this.salarioBase = salarioBase;
        this.monto = monto;
        this.plazo = plazo;
        this.tasaInteres = tasaInteres;
        this.idTipoPrestamo = idTipoPrestamo;
        this.tipoPrestamo = tipoPrestamo;
        this.idEstado = idEstado;
        this.estadoSolicitud = estadoSolicitud;
        this.deudaTotalMensual = calcularDeudaTotalMensual();
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public Integer getIdTipoPrestamo() {
        return idTipoPrestamo;
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public BigDecimal getDeudaTotalMensual() {
        return deudaTotalMensual;
    }

    private BigDecimal calcularDeudaTotalMensual() {
        BigDecimal tasaMensual = tasaInteres.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal unoPlusI = BigDecimal.ONE.add(tasaMensual);
        BigDecimal denominador = unoPlusI.pow(plazo);
        BigDecimal factor = denominador.subtract(BigDecimal.ONE).divide(denominador, 10, RoundingMode.HALF_UP);

        return monto.multiply(tasaMensual).divide(factor, 2, RoundingMode.HALF_UP);
    }
}