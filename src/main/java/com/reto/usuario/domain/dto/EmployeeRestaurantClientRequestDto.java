package com.reto.usuario.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRestaurantClientRequestDto {

    private Long idUserEmployee;
    private Long idRestaurant;
}
