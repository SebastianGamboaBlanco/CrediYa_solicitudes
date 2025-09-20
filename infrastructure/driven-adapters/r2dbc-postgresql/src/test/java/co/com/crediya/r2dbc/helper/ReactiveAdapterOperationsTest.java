package co.com.crediya.r2dbc.helper;

import co.com.crediya.model.LoanApplication;
import co.com.crediya.r2dbc.ApplicationRepositoryAdapter;
import co.com.crediya.r2dbc.ApplicationReactiveRepository;
import co.com.crediya.r2dbc.TypeReactiveRepository;
import co.com.crediya.r2dbc.entities.ApplicationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactiveAdapterOperationsTest {

    @Mock
    private ApplicationReactiveRepository applicationRepository;
    
    @Mock
    private TypeReactiveRepository typeRepository;
    
    private ApplicationRepositoryAdapter repositoryAdapter;

    @BeforeEach
    void setUp() {
        //repositoryAdapter = new ApplicationRepositoryAdapter(applicationRepository, typeRepository);
    }

    @Test
    void testCreateLoanApplication() {
        // Given
        LoanApplication loanApplication = new LoanApplication(
                new BigDecimal("10000"),
                12,
                "test@example.com",
                1,
                1
        );
        
        ApplicationEntity savedEntity = ApplicationEntity.builder()
                .applicationId(1)
                .amount(new BigDecimal("10000"))
                .termMonths(12)
                .email("test@example.com")
                .statusId(1)
                .typeId(1)
                .build();

        when(applicationRepository.save(any(ApplicationEntity.class)))
                .thenReturn(Mono.just(savedEntity));

        // When & Then
        StepVerifier.create(repositoryAdapter.create(loanApplication))
                .verifyComplete();
    }

    @Test
    void testExistsLoanTypeId() {
        // Given
        Integer loanTypeId = 1;
        when(typeRepository.existsById(loanTypeId))
                .thenReturn(Mono.just(true));

        // When & Then
        StepVerifier.create(repositoryAdapter.existsLoanTypeId(loanTypeId))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testExistsLoanTypeIdWhenNotExists() {
        // Given
        Integer loanTypeId = 999;
        when(typeRepository.existsById(loanTypeId))
                .thenReturn(Mono.just(false));

        // When & Then
        StepVerifier.create(repositoryAdapter.existsLoanTypeId(loanTypeId))
                .expectNext(false)
                .verifyComplete();
    }

}
