package co.com.crediya.model.exceptions;

public enum TipoError {
    USUARIO_NO_ENCONTRADO("Usuario no encontrado con el documento proporcionado"),
    TIPO_PRESTAMO_INVALIDO("El tipo de préstamo no existe o no está disponible"),
    DATOS_INVALIDOS("Los datos proporcionados no son válidos"),
    ERROR_SOLICITUD("Error procesando la solicitud"),
    CAMPO_REQUERIDO("Campo Requerido"),
    CAMPO_INVALIDO("Campo invalido");
    
    private final String mensaje;
    
    TipoError(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public String getMensaje() {
        return mensaje;
    }
}