package com.reto.usuario.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRestaurantClientRequestDto {

    private Long idOwnerRestaurant;
    private Long employeeUserId;
}
