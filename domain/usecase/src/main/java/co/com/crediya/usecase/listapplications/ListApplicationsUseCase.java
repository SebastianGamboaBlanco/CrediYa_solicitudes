package co.com.crediya.usecase.listapplications;

import co.com.crediya.model.PaginatedListApplications;
import co.com.crediya.model.exceptions.LoanApplicationException;
import co.com.crediya.model.exceptions.ErrorType;
import co.com.crediya.model.gateways.ApplicationRepository;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class ListApplicationsUseCase {
    private final ApplicationRepository applicationRepository;

    private static final List<Integer> ALLOWED_STATUS = Arrays.asList(1, 3, 4);

    public ListApplicationsUseCase(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Mono<PaginatedListApplications> listApplications(Integer userRoleId, Integer page, Integer size, List<Integer> statusIds) {
        List<Integer> finalStatusIds = (statusIds != null && !statusIds.isEmpty()) ? statusIds : ALLOWED_STATUS;

        return validateUserRole(userRoleId)
                .then(applicationRepository.findApplicationsByStatusIds(finalStatusIds, page, size));
    }

    private Mono<Void> validateUserRole(Integer roleId) {
        if (roleId == null || (!roleId.equals(2))) {
            return Mono.error(new LoanApplicationException(ErrorType.FORBIDDEN,
                    "role not allowed " + roleId));
        }
        return Mono.empty();
    }
}