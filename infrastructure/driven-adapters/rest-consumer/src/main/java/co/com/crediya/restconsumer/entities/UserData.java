package co.com.crediya.restconsumer.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private Integer id;
    private String firstName;
    private String lastName;

@JsonProperty("email")
    private String email;

@JsonProperty("identityDocument")
    private String identityDocument;

@JsonProperty("birthDate")
    private String birthDate;

    private String phone;

@JsonProperty("roleId")
    private Integer roleId;

@JsonProperty("baseSalary")
    private BigDecimal baseSalary;
}
