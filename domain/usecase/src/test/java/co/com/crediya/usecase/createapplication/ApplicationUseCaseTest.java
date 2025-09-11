package co.com.crediya.usecase.createapplication;

import co.com.crediya.model.LoanApplication;
import co.com.crediya.model.User;
import co.com.crediya.model.exceptions.LoanApplicationException;
import co.com.crediya.model.exceptions.ErrorType;
import co.com.crediya.model.gateways.ApplicationRepository;
import co.com.crediya.model.gateways.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationUseCaseTest {

    @Mock
    private UserService userService;

    @Mock
    private ApplicationRepository applicationRepository;

    private ApplicationUseCase useCase;

    private static final String IDENTITY_DOCUMENT = "12345678";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100000);
    private static final Integer TERM_MONTHS = 12;
    private static final Integer LOAN_TYPE_ID = 1;
    private static final String USER_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        useCase = new ApplicationUseCase(userService, applicationRepository);
    }

    @Test
    void create_WhenUserExistsAndLoanTypeValid_ShouldReturnSuccessMessage() {

        User user = new User(USER_EMAIL);
        when(userService.getUserByDocument(IDENTITY_DOCUMENT))
                .thenReturn(Mono.just(user));
        when(applicationRepository.existsLoanTypeId(LOAN_TYPE_ID))
                .thenReturn(Mono.just(true));
        when(applicationRepository.create(any(LoanApplication.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.create(IDENTITY_DOCUMENT, AMOUNT, TERM_MONTHS, LOAN_TYPE_ID))
                .expectNext("Pending review")
                .verifyComplete();

        verify(userService).getUserByDocument(IDENTITY_DOCUMENT);
        verify(applicationRepository).existsLoanTypeId(LOAN_TYPE_ID);
        verify(applicationRepository).create(any(LoanApplication.class));
    }

    @Test
    void create_WhenUserNotExists_ShouldThrowLoanApplicationException() {

        when(userService.getUserByDocument(IDENTITY_DOCUMENT))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.create(IDENTITY_DOCUMENT, AMOUNT, TERM_MONTHS, LOAN_TYPE_ID))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.USER_NOT_FOUND &&
                                error.getMessage().contains(IDENTITY_DOCUMENT)
                )
                .verify();

        verify(userService).getUserByDocument(IDENTITY_DOCUMENT);
    }

    @Test
    void create_WhenLoanTypeInvalid_ShouldThrowLoanApplicationException() {

        User user = new User(USER_EMAIL);
        when(userService.getUserByDocument(IDENTITY_DOCUMENT))
                .thenReturn(Mono.just(user));
        when(applicationRepository.existsLoanTypeId(LOAN_TYPE_ID))
                .thenReturn(Mono.just(false));

        StepVerifier.create(useCase.create(IDENTITY_DOCUMENT, AMOUNT, TERM_MONTHS, LOAN_TYPE_ID))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.INVALID_LOAN_TYPE &&
                                error.getMessage().contains("ID: " + LOAN_TYPE_ID)
                )
                .verify();

        verify(userService).getUserByDocument(IDENTITY_DOCUMENT);
        verify(applicationRepository).existsLoanTypeId(LOAN_TYPE_ID);
    }

    @Test
    void create_WhenRepositoryFails_ShouldPropagateError() {

        User user = new User(USER_EMAIL);
        RuntimeException repositoryError = new RuntimeException("Database error");

        when(userService.getUserByDocument(IDENTITY_DOCUMENT))
                .thenReturn(Mono.just(user));
        when(applicationRepository.existsLoanTypeId(LOAN_TYPE_ID))
                .thenReturn(Mono.just(true));
        when(applicationRepository.create(any(LoanApplication.class)))
                .thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.create(IDENTITY_DOCUMENT, AMOUNT, TERM_MONTHS, LOAN_TYPE_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(userService).getUserByDocument(IDENTITY_DOCUMENT);
        verify(applicationRepository).existsLoanTypeId(LOAN_TYPE_ID);
        verify(applicationRepository).create(any(LoanApplication.class));
    }

    @Test
    void create_WhenUserServiceFails_ShouldPropagateError() {

        RuntimeException serviceError = new RuntimeException("Service error");
        when(userService.getUserByDocument(IDENTITY_DOCUMENT))
                .thenReturn(Mono.error(serviceError));

        StepVerifier.create(useCase.create(IDENTITY_DOCUMENT, AMOUNT, TERM_MONTHS, LOAN_TYPE_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(userService).getUserByDocument(IDENTITY_DOCUMENT);
    }

    @Test
    void create_WhenLoanTypeValidationFails_ShouldPropagateError() {

        User user = new User(USER_EMAIL);
        RuntimeException validationError = new RuntimeException("Validation error");

        when(userService.getUserByDocument(IDENTITY_DOCUMENT))
                .thenReturn(Mono.just(user));
        when(applicationRepository.existsLoanTypeId(LOAN_TYPE_ID))
                .thenReturn(Mono.error(validationError));

        StepVerifier.create(useCase.create(IDENTITY_DOCUMENT, AMOUNT, TERM_MONTHS, LOAN_TYPE_ID))
                .expectError(RuntimeException.class)
                .verify();

        verify(userService).getUserByDocument(IDENTITY_DOCUMENT);
        verify(applicationRepository).existsLoanTypeId(LOAN_TYPE_ID);
    }

    @Test
    void create_ShouldCreateApplicationWithCorrectData() {

        User user = new User(USER_EMAIL);
        when(userService.getUserByDocument(IDENTITY_DOCUMENT))
                .thenReturn(Mono.just(user));
        when(applicationRepository.existsLoanTypeId(LOAN_TYPE_ID))
                .thenReturn(Mono.just(true));
        when(applicationRepository.create(any(LoanApplication.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.create(IDENTITY_DOCUMENT, AMOUNT, TERM_MONTHS, LOAN_TYPE_ID))
                .expectNext("Pending review")
                .verifyComplete();

        verify(applicationRepository).create(any(LoanApplication.class));
    }
}