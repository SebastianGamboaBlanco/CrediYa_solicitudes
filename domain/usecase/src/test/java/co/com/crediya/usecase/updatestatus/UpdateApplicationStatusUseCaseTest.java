package co.com.crediya.usecase.updatestatus;

import co.com.crediya.model.ApplicationStatusInfo;
import co.com.crediya.model.exceptions.LoanApplicationException;
import co.com.crediya.model.exceptions.ErrorType;
import co.com.crediya.model.gateways.ApplicationRepository;
import co.com.crediya.model.gateways.MessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateApplicationStatusUseCaseTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private MessageSender messageSender;

    private UpdateApplicationStatusUseCase useCase;

    private static final Integer APPLICATION_ID = 1;
    private static final Integer VALID_STATUS_ID_APPROVED = 2;
    private static final Integer VALID_STATUS_ID_REJECTED = 3;
    private static final Integer ASESOR_ROLE = 2;
    private static final String SUCCESS_MESSAGE = "Application status updated successfully";

    @BeforeEach
    void setUp() {
        useCase = new UpdateApplicationStatusUseCase(applicationRepository, messageSender);
    }

    @Test
    void updateApplicationStatus_WhenAllValidationsPassAndApproved_ShouldReturnSuccessMessage() {
        ApplicationStatusInfo statusInfo = new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Approved");

        when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(true));
        when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED))
                .thenReturn(Mono.empty());
        when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(statusInfo));
        when(messageSender.sendStatusUpdate(statusInfo))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED, ASESOR_ROLE))
                .expectNext(SUCCESS_MESSAGE)
                .verifyComplete();

        verify(applicationRepository).existsApplicationId(APPLICATION_ID);
        verify(applicationRepository).updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED);
        verify(applicationRepository).getApplicationWithStatus(APPLICATION_ID);
        verify(messageSender).sendStatusUpdate(statusInfo);
    }

    @Test
    void updateApplicationStatus_WhenAllValidationsPassAndRejected_ShouldReturnSuccessMessage() {
        ApplicationStatusInfo statusInfo = new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Rejected");

        when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(true));
        when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_REJECTED))
                .thenReturn(Mono.empty());
        when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(statusInfo));
        when(messageSender.sendStatusUpdate(statusInfo))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_REJECTED, ASESOR_ROLE))
                .expectNext(SUCCESS_MESSAGE)
                .verifyComplete();

        verify(applicationRepository).existsApplicationId(APPLICATION_ID);
        verify(applicationRepository).updateStatus(APPLICATION_ID, VALID_STATUS_ID_REJECTED);
        verify(applicationRepository).getApplicationWithStatus(APPLICATION_ID);
        verify(messageSender).sendStatusUpdate(statusInfo);
    }

    @Test
    void updateApplicationStatus_WhenInvalidUserRole_ShouldThrowForbiddenException() {
        Integer invalidRole = 3;

        // Mock needed because reactive flow continues to next operations in chain construction
        lenient().when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(true));
        lenient().when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED))
                .thenReturn(Mono.empty());
        lenient().when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Test")));
        lenient().when(messageSender.sendStatusUpdate(any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED, invalidRole))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.FORBIDDEN &&
                                error.getMessage().contains("Admin role required for status updates") &&
                                error.getMessage().contains("Current role ID: " + invalidRole)
                )
                .verify();
    }

    @Test
    void updateApplicationStatus_WhenNullUserRole_ShouldThrowForbiddenException() {
        // Mock needed because reactive flow continues to next operations in chain construction
        lenient().when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(true));
        lenient().when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED))
                .thenReturn(Mono.empty());
        lenient().when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Test")));
        lenient().when(messageSender.sendStatusUpdate(any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED, null))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.FORBIDDEN &&
                                error.getMessage().contains("Admin role required for status updates") &&
                                error.getMessage().contains("Current role ID: null")
                )
                .verify();
    }

    @Test
    void updateApplicationStatus_WhenInvalidStatusId_ShouldThrowInvalidStatusException() {
        Integer invalidStatusId = 1;

        // Mock needed because reactive flow continues to next operations in chain construction
        lenient().when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(true));
        lenient().when(applicationRepository.updateStatus(APPLICATION_ID, invalidStatusId))
                .thenReturn(Mono.empty());
        lenient().when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Test")));
        lenient().when(messageSender.sendStatusUpdate(any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, invalidStatusId, ASESOR_ROLE))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.INVALID_STATUS &&
                                error.getMessage().contains("Invalid status ID. Allowed values: Aprobado o Rechazado.")
                )
                .verify();
    }

    @Test
    void updateApplicationStatus_WhenNullStatusId_ShouldThrowInvalidStatusException() {
        // Mock needed because reactive flow continues to next operations in chain construction
        lenient().when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(true));
        lenient().when(applicationRepository.updateStatus(APPLICATION_ID, null))
                .thenReturn(Mono.empty());
        lenient().when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Test")));
        lenient().when(messageSender.sendStatusUpdate(any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, null, ASESOR_ROLE))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.INVALID_STATUS &&
                                error.getMessage().contains("Invalid status ID. Allowed values: Aprobado o Rechazado.")
                )
                .verify();
    }

    @Test
    void updateApplicationStatus_WhenApplicationNotExists_ShouldThrowApplicationNotFoundException() {
        when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(false));
        // Mock needed because reactive flow continues to next operations in chain construction
        when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED))
                .thenReturn(Mono.empty());
        when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Test")));

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED, ASESOR_ROLE))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.APPLICATION_NOT_FOUND &&
                                error.getMessage().contains("Application not found with ID: " + APPLICATION_ID)
                )
                .verify();

        verify(applicationRepository).existsApplicationId(APPLICATION_ID);
    }

    @Test
    void updateApplicationStatus_WhenApplicationExistsReturnsEmpty_ShouldThrowApplicationNotFoundException() {
        when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.empty());
        // Mock needed because reactive flow continues to next operations in chain construction
        when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED))
                .thenReturn(Mono.empty());
        when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Test")));

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED, ASESOR_ROLE))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.APPLICATION_NOT_FOUND &&
                                error.getMessage().contains("Application not found with ID: " + APPLICATION_ID)
                )
                .verify();

        verify(applicationRepository).existsApplicationId(APPLICATION_ID);
    }

    @Test
    void updateApplicationStatus_WhenRepositoryUpdateFails_ShouldPropagateError() {
        RuntimeException repositoryError = new RuntimeException("Database error");

        when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(true));
        when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED))
                .thenReturn(Mono.error(repositoryError));
        // Mock needed because reactive flow continues to next operations in chain construction
        when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Test")));

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED, ASESOR_ROLE))
                .expectError(RuntimeException.class)
                .verify();

        verify(applicationRepository).existsApplicationId(APPLICATION_ID);
        verify(applicationRepository).updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED);
    }

    @Test
    void updateApplicationStatus_WhenGetApplicationWithStatusFails_ShouldNotFailMainFlowBecauseOfOnErrorResume() {
        RuntimeException repositoryError = new RuntimeException("Database error");

        when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(true));
        when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED))
                .thenReturn(Mono.empty());
        when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED, ASESOR_ROLE))
                .expectNext(SUCCESS_MESSAGE)
                .verifyComplete();

        verify(applicationRepository).existsApplicationId(APPLICATION_ID);
        verify(applicationRepository).updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED);
        verify(applicationRepository).getApplicationWithStatus(APPLICATION_ID);
    }

    @Test
    void updateApplicationStatus_WhenMessageSenderFails_ShouldNotFailMainFlow() {
        ApplicationStatusInfo statusInfo = new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Approved");
        RuntimeException messagingError = new RuntimeException("SQS error");

        when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.just(true));
        when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED))
                .thenReturn(Mono.empty());
        when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(statusInfo));
        when(messageSender.sendStatusUpdate(statusInfo))
                .thenReturn(Mono.error(messagingError));

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED, ASESOR_ROLE))
                .expectNext(SUCCESS_MESSAGE)
                .verifyComplete();

        verify(applicationRepository).existsApplicationId(APPLICATION_ID);
        verify(applicationRepository).updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED);
        verify(applicationRepository).getApplicationWithStatus(APPLICATION_ID);
        verify(messageSender).sendStatusUpdate(statusInfo);
    }

    @Test
    void updateApplicationStatus_WhenExistsApplicationIdFails_ShouldPropagateError() {
        RuntimeException repositoryError = new RuntimeException("Database connection error");

        when(applicationRepository.existsApplicationId(APPLICATION_ID))
                .thenReturn(Mono.error(repositoryError));
        // Mock needed because reactive flow continues to next operations in chain construction
        when(applicationRepository.updateStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED))
                .thenReturn(Mono.empty());
        when(applicationRepository.getApplicationWithStatus(APPLICATION_ID))
                .thenReturn(Mono.just(new ApplicationStatusInfo(APPLICATION_ID, "test@example.com", "Test")));

        StepVerifier.create(useCase.updateApplicationStatus(APPLICATION_ID, VALID_STATUS_ID_APPROVED, ASESOR_ROLE))
                .expectError(RuntimeException.class)
                .verify();

        verify(applicationRepository).existsApplicationId(APPLICATION_ID);
    }
}