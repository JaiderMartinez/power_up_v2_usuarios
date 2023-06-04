package com.reto.usuario.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithFieldIdUserResponseDto {

    private Long idUser;
    private String name;
    private String lastName;
    private Long identificationDocument;
    private String cellPhone;
    private String email;
    private String rol;
}
