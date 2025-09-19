package co.com.crediya.usecase.listapplications;

import co.com.crediya.model.ListApplications;
import co.com.crediya.model.PaginatedListApplications;
import co.com.crediya.model.exceptions.LoanApplicationException;
import co.com.crediya.model.exceptions.ErrorType;
import co.com.crediya.model.gateways.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListApplicationsUseCaseTest {

    @Mock
    private ApplicationRepository applicationRepository;

    private ListApplicationsUseCase useCase;

    private static final Integer VALID_USER_ROLE_ID = 2;
    private static final Integer INVALID_USER_ROLE_ID = 3;
    private static final Integer PAGE = 1;
    private static final Integer SIZE = 10;
    private static final List<Integer> CUSTOM_STATUS_IDS = Arrays.asList(1, 2);
    private static final List<Integer> DEFAULT_STATUS_IDS = Arrays.asList(1, 3, 4);

    @BeforeEach
    void setUp() {
        useCase = new ListApplicationsUseCase(applicationRepository);
    }

    @Test
    void listApplications_WhenValidRoleAndCustomStatusIds_ShouldReturnApplications() {
        PaginatedListApplications expectedResult = createMockPaginatedApplications();

        when(applicationRepository.findApplicationsByStatusIds(eq(CUSTOM_STATUS_IDS), eq(PAGE), eq(SIZE)))
                .thenReturn(Mono.just(expectedResult));

        StepVerifier.create(useCase.listApplications(VALID_USER_ROLE_ID, PAGE, SIZE, CUSTOM_STATUS_IDS))
                .expectNext(expectedResult)
                .verifyComplete();

        verify(applicationRepository).findApplicationsByStatusIds(CUSTOM_STATUS_IDS, PAGE, SIZE);
    }

    @Test
    void listApplications_WhenValidRoleAndNullStatusIds_ShouldUseDefaultStatusIds() {
        PaginatedListApplications expectedResult = createMockPaginatedApplications();

        when(applicationRepository.findApplicationsByStatusIds(eq(DEFAULT_STATUS_IDS), eq(PAGE), eq(SIZE)))
                .thenReturn(Mono.just(expectedResult));

        StepVerifier.create(useCase.listApplications(VALID_USER_ROLE_ID, PAGE, SIZE, null))
                .expectNext(expectedResult)
                .verifyComplete();

        verify(applicationRepository).findApplicationsByStatusIds(DEFAULT_STATUS_IDS, PAGE, SIZE);
    }

    @Test
    void listApplications_WhenValidRoleAndEmptyStatusIds_ShouldUseDefaultStatusIds() {

        PaginatedListApplications expectedResult = createMockPaginatedApplications();

        when(applicationRepository.findApplicationsByStatusIds(eq(DEFAULT_STATUS_IDS), eq(PAGE), eq(SIZE)))
                .thenReturn(Mono.just(expectedResult));

        StepVerifier.create(useCase.listApplications(VALID_USER_ROLE_ID, PAGE, SIZE, Arrays.asList()))
                .expectNext(expectedResult)
                .verifyComplete();

        verify(applicationRepository).findApplicationsByStatusIds(DEFAULT_STATUS_IDS, PAGE, SIZE);
    }

    @Test
    void listApplications_WhenInvalidUserRole_ShouldThrowLoanApplicationException() {

        PaginatedListApplications mockResult = createMockPaginatedApplications();
        when(applicationRepository.findApplicationsByStatusIds(any(), any(), any()))
                .thenReturn(Mono.just(mockResult));

        StepVerifier.create(useCase.listApplications(INVALID_USER_ROLE_ID, PAGE, SIZE, CUSTOM_STATUS_IDS))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.FORBIDDEN &&
                                error.getMessage().contains("role not allowed")
                )
                .verify();
    }

    @Test
    void listApplications_WhenNullUserRole_ShouldThrowLoanApplicationException() {

        PaginatedListApplications mockResult = createMockPaginatedApplications();
        when(applicationRepository.findApplicationsByStatusIds(any(), any(), any()))
                .thenReturn(Mono.just(mockResult));

        StepVerifier.create(useCase.listApplications(null, PAGE, SIZE, CUSTOM_STATUS_IDS))
                .expectErrorMatches(error ->
                        error instanceof LoanApplicationException &&
                                ((LoanApplicationException) error).getErrorType() == ErrorType.FORBIDDEN &&
                                error.getMessage().contains("role not allowed")
                )
                .verify();
    }

    @Test
    void listApplications_WhenRepositoryFails_ShouldPropagateError() {

        RuntimeException repositoryError = new RuntimeException("Database error");

        when(applicationRepository.findApplicationsByStatusIds(any(), any(), any()))
                .thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.listApplications(VALID_USER_ROLE_ID, PAGE, SIZE, CUSTOM_STATUS_IDS))
                .expectError(RuntimeException.class)
                .verify();

        verify(applicationRepository).findApplicationsByStatusIds(CUSTOM_STATUS_IDS, PAGE, SIZE);
    }

    @Test
    void listApplications_WhenValidRoleWithNullPageAndSize_ShouldPassNullValues() {

        PaginatedListApplications expectedResult = createMockPaginatedApplications();

        when(applicationRepository.findApplicationsByStatusIds(eq(DEFAULT_STATUS_IDS), eq(null), eq(null)))
                .thenReturn(Mono.just(expectedResult));

        StepVerifier.create(useCase.listApplications(VALID_USER_ROLE_ID, null, null, null))
                .expectNext(expectedResult)
                .verifyComplete();

        verify(applicationRepository).findApplicationsByStatusIds(DEFAULT_STATUS_IDS, null, null);
    }

    private PaginatedListApplications createMockPaginatedApplications() {
        ListApplications application1 = new ListApplications(
                1,
                "Juan Pérez",
                "juan.perez@example.com",
                BigDecimal.valueOf(3000000),
                BigDecimal.valueOf(10000000),
                36,
                BigDecimal.valueOf(15.5),
                1,
                "Hipotecario",
                1,
                "En revisión"
        );

        ListApplications application2 = new ListApplications(
                2,
                "María García",
                "maria.garcia@example.com",
                BigDecimal.valueOf(2500000),
                BigDecimal.valueOf(5000000),
                24,
                BigDecimal.valueOf(12.0),
                2,
                "Personal",
                3,
                "Aprobado"
        );

        return new PaginatedListApplications(
                Arrays.asList(application1, application2),
                50,
                1,
                10
        );
    }
}