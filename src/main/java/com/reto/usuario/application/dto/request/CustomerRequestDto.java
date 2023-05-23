package com.reto.usuario.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDto {

    private String name;
    private String lastName;
    private Long identificationDocument;
    private String cellPhone;
    private String email;
    private String password;
    private Long idRol;
}
