package co.com.crediya.model.exceptions;

public enum ErrorType {
    USER_NOT_FOUND("User not found with the provided document"),
    INVALID_LOAN_TYPE("The loan type does not exist or is not available"),
    INVALID_DATA("The provided data is not valid"),
    APPLICATION_ERROR("Error processing the application"),
    REQUIRED_FIELD("Required Field"),
    INVALID_FIELD("Invalid field"),
    UNAUTHORIZED("Authentication required"),
    FORBIDDEN("Access denied - Insufficient permissions"),
    INVALID_TOKEN("Invalid or expired authentication token"),
    INVALID_ROLE("Invalid user role for this operation"),
    INVALID_STATUS("Invalid status value for this operation"),
    APPLICATION_NOT_FOUND("Application not found");
    
    private final String message;
    
    ErrorType(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}