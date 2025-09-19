package co.com.crediya.model;

import java.math.BigDecimal;

public class User {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final BigDecimal baseSalary;

    public User(String email) {
        this.email = email;
        this.firstName = null;
        this.lastName = null;
        this.baseSalary = null;
    }

    public User(String email, String firstName, String lastName, BigDecimal baseSalary) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.baseSalary = baseSalary;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return null;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}