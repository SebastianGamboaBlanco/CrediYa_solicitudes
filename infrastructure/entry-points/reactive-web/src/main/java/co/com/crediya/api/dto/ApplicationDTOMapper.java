package co.com.crediya.api.dto;

public class ApplicationDTOMapper {
    
    public static ApplicationResponse toSuccessResponse(String message) {
        return ApplicationResponse.success(message);
    }
    
    @Deprecated
    public static ApplicationResponse toResponse(Integer status, String message) {
        return ApplicationResponse.builder()
                .code(status == 200 ? 0 : 1)
                .message(message)
                .build();
    }
}