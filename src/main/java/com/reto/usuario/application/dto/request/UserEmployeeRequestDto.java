package com.reto.usuario.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEmployeeRequestDto {

    private String name;
    private String lastName;
    private Long identificationDocument;
    private String cellPhone;
    private String email;
    private String password;
    private Long idRol;
    private Long idRestaurant;
}
