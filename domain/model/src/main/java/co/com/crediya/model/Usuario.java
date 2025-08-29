package co.com.crediya.model;

public class Usuario {
    private final String email;
    
    public Usuario(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}