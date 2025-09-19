package co.com.crediya.r2dbc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedApplicationsResponse {
    private List<ListApplicationsQueryResult> applications;
    private Integer totalRecords;
    private Integer pageNumber;
    private Integer pageSize;
}