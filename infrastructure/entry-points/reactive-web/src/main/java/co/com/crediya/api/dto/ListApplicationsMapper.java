package co.com.crediya.api.dto;

import co.com.crediya.model.PaginatedListApplications;
import co.com.crediya.model.ListApplications;

import java.util.stream.Collectors;

public class ListApplicationsMapper {

    public static ListApplicationsResponse toResponse(PaginatedListApplications paginatedListApplications) {
        return ListApplicationsResponse.builder()
                .applications(paginatedListApplications.getApplications().stream()
                        .map(ListApplicationsMapper::toApplicationItemResponse)
                        .collect(Collectors.toList()))
                .totalRecords(paginatedListApplications.getTotalRecords())
                .pageNumber(paginatedListApplications.getPageNumber())
                .pageSize(paginatedListApplications.getPageSize())
                .build();
    }

    private static ApplicationItemResponse toApplicationItemResponse(ListApplications listApplications) {
        return ApplicationItemResponse.builder()
                .idSolicitud(listApplications.getIdSolicitud())
                .nombre(listApplications.getNombre())
                .email(listApplications.getEmail())
                .salarioBase(listApplications.getSalarioBase())
                .monto(listApplications.getMonto())
                .plazo(listApplications.getPlazo())
                .tasaInteres(listApplications.getTasaInteres())
                .idTipoPrestamo(listApplications.getIdTipoPrestamo())
                .tipoPrestamo(listApplications.getTipoPrestamo())
                .idEstado(listApplications.getIdEstado())
                .estadoSolicitud(listApplications.getEstadoSolicitud())
                .deudaTotalMensual(listApplications.getDeudaTotalMensual())
                .build();
    }
}