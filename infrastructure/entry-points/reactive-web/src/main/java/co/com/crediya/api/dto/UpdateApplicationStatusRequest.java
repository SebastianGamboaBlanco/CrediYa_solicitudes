package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Required data to update application status")
public class UpdateApplicationStatusRequest {

    @NotNull
    @Positive
    @Schema(description = "ID of the application to update",
            example = "123",
            required = true,
            minimum = "1")
    private Integer applicationId;

    @NotNull
    @Positive
    @Schema(description = "New status ID for the application (2=Approved, 3=Rejected)",
            example = "2",
            required = true,
            allowableValues = {"2", "3"})
    private Integer statusId;
}