package co.com.crediya.model.exceptions;

public class LoanApplicationException extends RuntimeException {
    private final ErrorType errorType;
    
    public LoanApplicationException(ErrorType errorType, String additionalDetail) {
        super(errorType.getMessage() + ": " + additionalDetail);
        this.errorType = errorType;
    }
    
    public LoanApplicationException(String message) {
        super(message);
        this.errorType = null;
    }
    
    public ErrorType getErrorType() {
        return errorType;
    }
}