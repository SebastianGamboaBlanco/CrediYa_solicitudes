package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Paginated application listing response")
public class ListApplicationsResponse {

    @Schema(description = "List of applications")
    private List<ApplicationItemResponse> applications;

    @Schema(description = "Total number of records", example = "150")
    private Integer totalRecords;

    @Schema(description = "Current page number", example = "1")
    private Integer pageNumber;

    @Schema(description = "Page size", example = "10")
    private Integer pageSize;
}