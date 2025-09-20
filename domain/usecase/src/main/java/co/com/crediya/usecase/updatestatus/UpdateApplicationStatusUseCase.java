package co.com.crediya.usecase.updatestatus;

import co.com.crediya.model.exceptions.LoanApplicationException;
import co.com.crediya.model.exceptions.ErrorType;
import co.com.crediya.model.gateways.ApplicationRepository;
import co.com.crediya.model.gateways.MessageSender;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class UpdateApplicationStatusUseCase {
    private final ApplicationRepository applicationRepository;
    private final MessageSender messageSender;

    private static final List<Integer> ALLOWED_STATUS_UPDATES = Arrays.asList(2, 3);
    private static final Integer ASESOR_ROLE = 2;

    public UpdateApplicationStatusUseCase(ApplicationRepository applicationRepository, MessageSender messageSender) {
        this.applicationRepository = applicationRepository;
        this.messageSender = messageSender;
    }

    public Mono<String> updateApplicationStatus(Integer applicationId, Integer statusId, Integer userRoleId) {
        return validateUserRole(userRoleId)
                .then(validateStatusId(statusId))
                .then(validateApplicationExists(applicationId))
                .then(applicationRepository.updateStatus(applicationId, statusId))
                .then(sendStatusUpdateNotification(applicationId))
                .then(Mono.just("Application status updated successfully"));
    }

    private Mono<Void> validateUserRole(Integer roleId) {
        if (roleId == null || !roleId.equals(ASESOR_ROLE)) {
            return Mono.error(new LoanApplicationException(ErrorType.FORBIDDEN,
                    "Admin role required for status updates. Current role ID: " + roleId));
        }
        return Mono.empty();
    }

    private Mono<Void> validateStatusId(Integer statusId) {
        if (statusId == null || !ALLOWED_STATUS_UPDATES.contains(statusId)) {
            return Mono.error(new LoanApplicationException(ErrorType.INVALID_STATUS,
                    "Invalid status ID. Allowed values: Aprobado o Rechazado."));
        }
        return Mono.empty();
    }

    private Mono<Void> validateApplicationExists(Integer applicationId) {
        return applicationRepository.existsApplicationId(applicationId)
                .filter(exists -> exists)
                .switchIfEmpty(Mono.error(new LoanApplicationException(ErrorType.APPLICATION_NOT_FOUND,
                        "Application not found with ID: " + applicationId)))
                .then();
    }

    private Mono<Void> sendStatusUpdateNotification(Integer applicationId) {
        return applicationRepository.getApplicationWithStatus(applicationId)
                .flatMap(messageSender::sendStatusUpdate)
                .onErrorResume(error -> Mono.empty()) // No fallar el flujo principal si SQS falla
                .then();
    }
}