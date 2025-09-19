package co.com.crediya.api;

import co.com.crediya.api.dto.ApplicationRequest;
import co.com.crediya.api.dto.ApplicationResponse;
import co.com.crediya.api.dto.ListApplicationsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/applications",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "createApplication",
                    operation = @Operation(
                            operationId = "createApplication",
                            summary = "Register loan application",
                            description = "Allows registering a new loan application by validating user existence and loan type",
                            tags = {"Applications"},
                            requestBody = @RequestBody(
                                    description = "Loan application data",
                                    required = true,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = ApplicationRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Loan application registered successfully",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ApplicationResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error in submitted data or business validation",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ApplicationResponse.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "BadRequest",
                                                                    value = """
                                                                            {
                                                                              "code": 1,
                                                                              "errores": [
                                                                                {
                                                                                  "field": "identityDocument",
                                                                                  "message": "must not be empty",
                                                                                  "rejectedValue": null
                                                                                }
                                                                              ]
                                                                            }
                                                                            """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ApplicationResponse.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "InternalError",
                                                                    value = "{ \"code\": 1, \"message\": \"Internal server error\" }"
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "503",
                                            description = "External service unavailable",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ApplicationResponse.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "ServiceUnavailable",
                                                                    value = "{ \"code\": 1, \"message\": \"External service unavailable\" }"
                                                            )
                                                    }
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/applications",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listApplications",
                    operation = @Operation(
                            operationId = "listApplications",
                            summary = "List loan applications",
                            description = "Retrieves a paginated list of loan applications with optional filters",
                            tags = {"Applications"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Applications retrieved successfully",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ListApplicationsResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Access forbidden - insufficient role permissions",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ApplicationResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ApplicationResponse.class)
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/applications")
                .and(accept(APPLICATION_JSON)), handler::createApplication)
                .andRoute(GET("/api/v1/applications"), handler::listApplications);
    }
}
