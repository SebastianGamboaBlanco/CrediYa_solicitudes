package co.com.crediya.model;

import java.math.BigDecimal;

public class LoanApplication {
    public static final Integer PENDING_STATUS = 1;

    private final BigDecimal amount;
    private final Integer termMonths;
    private final String email;
    private final Integer statusId;
    private final Integer loanTypeId;

    public LoanApplication(BigDecimal amount, Integer termMonths, String email, Integer statusId, Integer loanTypeId) {

        this.amount = amount;
        this.termMonths = termMonths;
        this.email = email;
        this.statusId = statusId;
        this.loanTypeId = loanTypeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public String getEmail() {
        return email;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public Integer getLoanTypeId() {
        return loanTypeId;
    }

}
