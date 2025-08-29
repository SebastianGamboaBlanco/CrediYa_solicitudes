package co.com.crediya.r2dbc.helpers;

import co.com.crediya.model.Solicitud;
import co.com.crediya.r2dbc.entities.SolicitudEntity;

public class SolicitudMapper {
    
    public static SolicitudEntity toEntity(Solicitud solicitud) {
        return SolicitudEntity.builder()
                .monto(solicitud.getMonto())
                .plazo(solicitud.getPlazo())
                .email(solicitud.getEmail())
                .idEstado(solicitud.getIdEstado())
                .idTipoPrestamo(solicitud.getIdTipoPrestamo())
                .build();
    }
    
    public static Solicitud toDomain(SolicitudEntity entity) {
        return new Solicitud(
                entity.getMonto(),
                entity.getPlazo(),
                entity.getEmail(),
                entity.getIdEstado(),
                entity.getIdTipoPrestamo()
        );
    }
}