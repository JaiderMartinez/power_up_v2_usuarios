package com.reto.usuario.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private String name;
    private String lastName;
    private Long identificationDocument;
    private String cellPhone;
    private String email;
    private String rol;
}
