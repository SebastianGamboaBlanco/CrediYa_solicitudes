package co.com.crediya.api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FieldError {

    private String campo;


    private String mensaje;


    private Object valorRechazado;
}
