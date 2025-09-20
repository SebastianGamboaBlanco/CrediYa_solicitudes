package co.com.crediya.model;

import java.time.LocalDateTime;

public class ApplicationStatusInfo {
    private final Integer applicationId;
    private final String email;
    private final String statusName;
    private final LocalDateTime timestamp;

    public ApplicationStatusInfo(Integer applicationId, String email, String statusName) {
        this.applicationId = applicationId;
        this.email = email;
        this.statusName = statusName;
        this.timestamp = LocalDateTime.now();
    }

    public ApplicationStatusInfo(Integer applicationId, String email, String statusName, LocalDateTime timestamp) {
        this.applicationId = applicationId;
        this.email = email;
        this.statusName = statusName;
        this.timestamp = timestamp;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public String getEmail() {
        return email;
    }

    public String getStatusName() {
        return statusName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}