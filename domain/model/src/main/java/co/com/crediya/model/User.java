package co.com.crediya.model;

public class User {
    private final String email;
    
    public User(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}