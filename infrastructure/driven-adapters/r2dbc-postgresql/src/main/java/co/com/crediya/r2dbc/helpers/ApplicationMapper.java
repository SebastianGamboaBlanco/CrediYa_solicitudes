package co.com.crediya.r2dbc.helpers;

import co.com.crediya.model.LoanApplication;
import co.com.crediya.r2dbc.entities.ApplicationEntity;

public class ApplicationMapper {
    
    public static ApplicationEntity toEntity(LoanApplication loanApplication) {
        return ApplicationEntity.builder()
                .amount(loanApplication.getAmount())
                .termMonths(loanApplication.getTermMonths())
                .email(loanApplication.getEmail())
                .statusId(loanApplication.getStatusId())
                .typeId(loanApplication.getLoanTypeId())
                .build();
    }
    
    public static LoanApplication toDomain(ApplicationEntity entity) {
        return new LoanApplication(
                entity.getAmount(),
                entity.getTermMonths(),
                entity.getEmail(),
                entity.getStatusId(),
                entity.getTypeId()
        );
    }
}