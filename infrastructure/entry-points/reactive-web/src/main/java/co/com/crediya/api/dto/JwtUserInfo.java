package co.com.crediya.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class JwtUserInfo {
    private final String identityDocument;
    private final String email;
    private final Integer roleId;
    private final String roleName;
    
    public boolean isClientRole() {
        return roleId != null && (roleId.equals(3));
    }
}