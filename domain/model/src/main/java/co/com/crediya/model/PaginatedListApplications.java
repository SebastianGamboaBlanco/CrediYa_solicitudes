package co.com.crediya.model;

import java.util.List;

public class PaginatedListApplications {
    private final List<ListApplications> applications;
    private final Integer totalRecords;
    private final Integer pageNumber;
    private final Integer pageSize;

    public PaginatedListApplications(List<ListApplications> applications, Integer totalRecords,
                                   Integer pageNumber, Integer pageSize) {
        this.applications = applications;
        this.totalRecords = totalRecords;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public List<ListApplications> getApplications() {
        return applications;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}