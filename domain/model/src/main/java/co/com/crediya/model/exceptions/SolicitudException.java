package co.com.crediya.model.exceptions;

public class SolicitudException extends RuntimeException {
    private final TipoError tipoError;
    
    public SolicitudException(TipoError tipoError, String detalleAdicional) {
        super(tipoError.getMensaje() + ": " + detalleAdicional);
        this.tipoError = tipoError;
    }
    
    public TipoError getTipoError() {
        return tipoError;
    }
}